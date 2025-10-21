{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

    flake-parts.url = "github:hercules-ci/flake-parts";

    systems.url = "github:nix-systems/default";

    import-tree.url = "github:vic/import-tree";

    devshell.url = "github:numtide/devshell";
    devshell.inputs.nixpkgs.follows = "nixpkgs";

    git-hooks-nix.url = "github:cachix/git-hooks.nix";

    build-gradle-application.url = "github:raphiz/buildGradleApplication";
    build-gradle-application.inputs.nixpkgs.follows = "nixpkgs";

    nix-option-search.url = "github:ciderale/nix-option-search";
    nix-option-search.inputs.nixpkgs.follows = "nixpkgs";
  };
  outputs = inputs:
    inputs.flake-parts.lib.mkFlake {inherit inputs;} {
      systems = import inputs.systems;
      imports = [./nix];
      perSystem = {pkgs, ...}: {
        _module.args.lib = pkgs.lib;
      };
    };
}
