{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

    systems.url = "github:nix-systems/default";

    blueprint.url = "github:numtide/blueprint";
    blueprint.inputs.nixpkgs.follows = "nixpkgs";
    blueprint.inputs.systems.follows = "systems";

    devshell.url = "github:numtide/devshell";
    devshell.inputs.nixpkgs.follows = "nixpkgs";
    devshell.inputs.systems.follows = "systems";

    git-hooks-nix.url = "github:cachix/git-hooks.nix";

    build-gradle-application.url = "github:raphiz/buildGradleApplication";
    build-gradle-application.inputs.nixpkgs.follows = "nixpkgs";
  };
  outputs = inputs:
    inputs.blueprint {
      inherit inputs;
      prefix = "nix";
    };
}
