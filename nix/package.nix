{
  lib,
  version,
  pkgs,
  buildGradleApplication,
  jdk25,
}:
buildGradleApplication {
  inherit version;
  pname = "kokus";

  jdk = jdk25;

  env = {
    DB_SKIP = "true";
  };

  buildTask = ":assemble";
  installLocation = "app/assembly/build/install/*/";

  # TODO: filter!
  src = ./..;

  meta = {
    license = lib.licenses.mit;
    description = "Kokus Recipe Management";
  };

  gradle = pkgs.gradle-packages.mkGradle {
    version = "9.2.1";
    hash = "sha256-cvRMn468sa9Dg49F7lxKqcVESJizRoqz9K97YHbFvD8=";
    defaultJava = jdk25;
  };
}
