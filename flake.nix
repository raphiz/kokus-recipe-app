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
    devShells = forAllSystems (
      system: pkgs: {
        default = nix-shell-parts.lib.mkShell {inherit pkgs;} {
          _module.args = {
            kokus = self.packages.${system}.kokus;
          };
          imports = [./nix/devshell.nix];
        };
      }
    );

    # === NixOS Modules ===
    nixosModules = {
      kokus = import ./nix/nixos-module.nix;
      default = self.nixosModules.kokus;
    };

    # === Checks ===
    checks = forAllSystems (system: pkgs: {
      module-test = import ./nix/nixos-module-test.nix {
        inherit pkgs;
        module = self.nixosModules.kokus;
        kokus = self.packages.${system}.kokus;
      };
    });
  };
}
