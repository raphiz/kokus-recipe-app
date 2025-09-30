{self, ...}: {
  perSystem = {
    inputs',
    config,
    pkgs,
    lib,
    ...
  }: {
    packages.kokus = let
      inherit (inputs'.build-gradle-application.legacyPackages) buildGradleApplication gradleFromWrapper;

      jdk = pkgs.temurin-bin-21;
      gradle = (gradleFromWrapper ../gradle/wrapper/gradle-wrapper.properties).override {java = jdk;};

      pname = "kokus";
    in
      buildGradleApplication {
        inherit pname jdk gradle;

        version = lib.removeSuffix "-dirty" (self.shortRev or self.dirtyShortRev);

        env = {
          DB_SKIP = "true";
        };

        buildTask = ":assemble";
        installLocation = "app/assembly/build/install/*/";

        # TODO: consider filtering here to prevent unnecessary rebuilds
        src = ./..;

        meta = {
          # set default for meta.mainProgram here to gain compatibility with:
          # `lib.getExe`, `nix run`, `nix bundle`, etc.
          mainProgram = pname; # TODO: Move this into buildGradleApplication? Then `pname` can be inlined.
          license = lib.licenses.mit;
          description = "Kokus Recipe Management";
        };
      };
  };
}
