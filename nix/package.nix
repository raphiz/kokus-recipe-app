{
  temurin-bin-21,
  gradleFromWrapper,
  buildGradleApplication,
  lib,
  version,
  ...
}: let
  jdk = temurin-bin-21;
  gradle = (gradleFromWrapper ../gradle/wrapper/gradle-wrapper.properties).override {
    java = jdk;
  };
in
  buildGradleApplication {
    pname = "kokus";

    inherit version jdk gradle;

    env = {
      DB_SKIP = "true";
    };

    buildTask = ":assemble";
    installLocation = "app/assembly/build/install/*/";

    # TODO: consider filtering here to prevent unnecessary rebuilds
    src = ./..;

    meta = {
      license = lib.licenses.mit;
      description = "Kokus Recipe Management";
    };
  }
