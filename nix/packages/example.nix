{
  pname,
  pkgs,
  perSystem,
}: let
  inherit (perSystem.build-gradle-application) buildGradleApplication gradleFromWrapper;
  inherit (pkgs) lib;
  defaultJava = pkgs.temurin-bin-21;
in
  buildGradleApplication {
    inherit pname;

    # TODO: Decide on a version schema
    # Specify it manually
    version = "1.0.0";
    # or set the commit hash as version
    # remove the `-dirty` suffix to avoid unnecessary rebuilds in local dev.
    # version = lib.removeSuffix "-dirty" (flake.shortRev or flake.dirtyShortRev);

    # TODO: consider filtering here to prevent unnecessary rebuilds
    src = ./../..;

    meta = {
      # set default for meta.mainProgram here to gain compatibility with:
      # `lib.getExe`, `nix run`, `nix bundle`, etc.
      mainProgram = pname;
      license = lib.licenses.mit; # TODO: Change license (if necessary)
      description = "Example Application";
    };

    jdk = defaultJava;
    gradle = gradleFromWrapper {
      wrapperPropertiesPath = ../../gradle/wrapper/gradle-wrapper.properties;
    };
  }
