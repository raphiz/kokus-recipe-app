{
  description = "Kokus Recipe Management";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

    systems.url = "github:nix-systems/default";

    nix-shell-parts.url = "github:ergon/nix-shell-parts";
    nix-shell-parts.inputs.nixpkgs.follows = "nixpkgs";

    build-gradle-application.url = "github:raphiz/buildGradleApplication";
    build-gradle-application.inputs.nixpkgs.follows = "nixpkgs";
  };

  outputs = {
    self,
    nixpkgs,
    systems,
    nix-shell-parts,
    build-gradle-application,
  }: let
    forAllSystems = function:
      nixpkgs.lib.genAttrs (import systems) (system: function system nixpkgs.legacyPackages.${system});

    version = self.shortRev or self.dirtyShortRev or "unknown";

    shellConfigs = forAllSystems (
      system: pkgs:
        nix-shell-parts.lib.evalShell {inherit pkgs;} {
          _module.args.kokus = self.packages.${system}.kokus;
          imports = [./nix/devshell.nix];
        }
    );
  in {
    # === Packages ===
    packages = forAllSystems (system: pkgs: {
      kokus = pkgs.callPackage ./nix/package.nix {
        inherit version;
        buildGradleApplication = build-gradle-application.legacyPackages.${system}.buildGradleApplication;
      };
      default = self.packages.${system}.kokus;
    });

    # === Dev Shells ===
    devShells = forAllSystems (system: _pkgs: {
      default = shellConfigs.${system}.finalPackage;
    });

    # === NixOS Modules ===
    nixosModules = {
      kokus = import ./nix/nixos-module.nix;
      default = self.nixosModules.kokus;
    };

    # === Checks ===
    checks = forAllSystems (system: pkgs: {
      treefmt = shellConfigs.${system}.treefmt.build.check self;
      module-test = import ./nix/nixos-module-test.nix {
        inherit pkgs;
        module = self.nixosModules.kokus;
        kokus = self.packages.${system}.kokus;
      };
    });

    # === Formatter ===
    formatter = forAllSystems (
      system: _pkgs:
        shellConfigs.${system}.treefmt.build.wrapper
    );
  };
}
