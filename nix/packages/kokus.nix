{
  flake,
  pname,
  pkgs,
  perSystem,
}: let
  inherit (perSystem.build-gradle-application) buildGradleApplication gradleFromWrapper;
  inherit (pkgs) lib;
  jdk = pkgs.temurin-bin-21;
in
  buildGradleApplication {
    inherit pname jdk;

    env = {
      DB_SKIP = "true";
    };

    buildTask = ":assemble";
    installLocation = "app/assembly/build/install/*/";

    # remove the `-dirty` suffix to avoid unnecessary rebuilds in local dev.
    version = lib.removeSuffix "-dirty" (flake.shortRev or flake.dirtyShortRev);

    # TODO: consider filtering here to prevent unnecessary rebuilds
    src = ./../..;

    meta = {
      # set default for meta.mainProgram here to gain compatibility with:
      # `lib.getExe`, `nix run`, `nix bundle`, etc.
      mainProgram = pname;
      license = lib.licenses.mit;
      description = "Kokus Recipe Management";
    };

    gradle = (gradleFromWrapper ../../gradle/wrapper/gradle-wrapper.properties).override {java = jdk;};
  }
