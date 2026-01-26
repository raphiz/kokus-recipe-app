{self, ...}: let
  version = self.shortRev or self.dirtyShortRev or "unknown";
in {
  perSystem = {
    config,
    pkgs,
    inputs',
    ...
  }: {
    packages = {
      kokus = pkgs.callPackage (./../packages/default.nix) {
        inherit version;
        inherit (inputs'.build-gradle-application.legacyPackages) buildGradleApplication;
      };
      default = config.packages.kokus;
    };
  };
}
