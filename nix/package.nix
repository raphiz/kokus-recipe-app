{
  lib,
  version,
  pkgs,
  buildGradleApplication,
  jdk26,
}:
buildGradleApplication {
  inherit version;
  pname = "kokus";

  jdk = jdk26;

  env = {
    DB_SKIP = "true";
  };

  buildTask = ":assemble";
  installLocation = "app/assembly/build/install/*/";

  src = let
    fs = lib.fileset;
  in
    fs.toSource {
      root = ./..;
      fileset = fs.unions [
        ./../app
        ./../gradle
        ./../build.gradle.kts
        ./../gradle.properties
        ./../settings.gradle.kts
      ];
    };

  meta = {
    license = lib.licenses.mit;
    description = "Kokus Recipe Management";
  };

  gradle = pkgs.gradle-packages.mkGradle {
    version = "9.2.1";
    hash = "sha256-cvRMn468sa9Dg49F7lxKqcVESJizRoqz9K97YHbFvD8=";
    defaultJava = jdk26;
  };
}
