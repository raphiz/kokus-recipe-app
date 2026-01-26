{inputs, ...}: {
  imports = [
    inputs.nix-shell-parts.flakeModules.default
  ];
  perSystem = {...}: {
    imports = [
      ./../devshells
    ];
  };
}
