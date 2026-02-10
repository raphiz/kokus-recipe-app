{
  description = "Kokus Recipe Management";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

    systems.url = "github:nix-systems/default";

    treefmt-nix.url = "github:numtide/treefmt-nix";
    treefmt-nix.inputs.nixpkgs.follows = "nixpkgs";

    nix-shell-parts.url = "github:ergon/nix-shell-parts";
    nix-shell-parts.inputs.nixpkgs.follows = "nixpkgs";
    nix-shell-parts.inputs.treefmt-nix.follows = "treefmt-nix";

    build-gradle-application.url = "github:raphiz/buildGradleApplication";
    build-gradle-application.inputs.nixpkgs.follows = "nixpkgs";
    build-gradle-application.inputs.systems.follows = "systems";
  };

  outputs = {
    self,
    nixpkgs,
    treefmt-nix,
    systems,
    nix-shell-parts,
    build-gradle-application,
  }: let
    forAllSystems = function:
      nixpkgs.lib.genAttrs (import systems) (system: function system nixpkgs.legacyPackages.${system});

    version = self.shortRev or self.dirtyShortRev or "unknown";

    treefmtEval = forAllSystems (
      system: pkgs:
        treefmt-nix.lib.evalModule pkgs (import ./nix/treefmt.nix {
          inherit pkgs;
          inherit (self.packages.${system}) kokus;
        })
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
    devShells = forAllSystems (system: pkgs: {
      default =
        nix-shell-parts.lib.mkShell {inherit pkgs;}
        {
          _module.args.kokus = self.packages.${system}.kokus;
          _module.args.treefmt-wrapper = treefmtEval.${system}.config.build.wrapper;
          imports = [./nix/devshell.nix];
        };
    });

    # === NixOS Modules ===
    nixosModules = {
      kokus = import ./nix/nixos-module.nix;
      default = self.nixosModules.kokus;
    };

    # === Checks ===
    checks = forAllSystems (system: pkgs: {
      treefmt = treefmtEval.${system}.config.build.check self;
      module-test = import ./nix/nixos-module-test.nix {
        inherit pkgs;
        module = self.nixosModules.kokus;
        kokus = self.packages.${system}.kokus;
      };
    });

    # === Formatter ===
    formatter = forAllSystems (
      system: _pkgs: treefmtEval.${system}.config.build.wrapper
    );
  };
}
