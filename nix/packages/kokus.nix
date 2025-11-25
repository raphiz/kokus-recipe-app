{
  flake,
  pname,
  pkgs,
  perSystem,
}: let
  inherit (perSystem.build-gradle-application) buildGradleApplication gradleFromWrapper;
  inherit (pkgs) lib;
  jdk = pkgs.temurin-bin-25;
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
      license = lib.licenses.mit;
      description = "Kokus Recipe Management";
    };

    gradle = gradleFromWrapper {
      wrapperPropertiesPath = ../../gradle/wrapper/gradle-wrapper.properties;
      defaultJava = jdk;
    };
  }
