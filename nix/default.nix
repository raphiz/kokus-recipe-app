{
  self,
  inputs,
  ...
}: {
  imports = [
    ./shell.nix
    ./linters.nix
    ./nixos-module.nix
    ./nixos-module.test.nix

    inputs.nix-option-search.modules.flake-parts
  ];

  perSystem = {
    inputs',
    config,
    lib,
    pkgs,
    ...
  }: {
    packages.kokus = pkgs.callPackage ./package.nix {
      inherit (inputs'.build-gradle-application.legacyPackages) buildGradleApplication gradleFromWrapper;
      jdk = pkgs.temurin-bin-21;
      version = lib.removeSuffix "-dirty" (self.shortRev or self.dirtyShortRev);
    };
    packages.default = config.packages.kokus;
  };
}
