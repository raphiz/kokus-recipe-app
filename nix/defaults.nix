{
  perSystem = {config, ...}: {
    packages.default = config.packages.kokus;
    devShells.default = config.devShells.kokus;
  };
}
