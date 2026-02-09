# devshell configured via https://github.com/ergon/nix-shell-parts
{
  config,
  lib,
  pkgs,
  kokus,
  treefmt-wrapper,
  ...
}: let
  jdk = kokus.jdk;
  gradle = kokus.gradle;
in {
  packages = [
    kokus.updateVerificationMetadata
    gradle
    jdk
    treefmt-wrapper
  ];

  # To make devcontainers work in intelliJ, we need a fixed path for JAVA_HOME and binaries (PATH)
  # we link them in the workspace root.
  profile.enable = true;
  git.root.enable = true;
  shellHook = let
    jdkDir = "${config.git.root.shellVariable}/.jdk";
  in ''
    rm -rf "${jdkDir}"
    ln -fs ${jdk.home} "${jdkDir}"
  '';

  env."JAVA_HOME" = jdk.home;

  git.hooks.pre-commit-command = "${lib.getExe treefmt-wrapper} --fail-on-change $FILES";

  scripts = {
    build.text = ''${lib.getExe gradle} :clean :check :assemble'';
    build-continuously.text = ''${lib.getExe gradle} --continuous :check :assemble'';
    rundev.text = ''${lib.getExe gradle} :run'';
    module-test.text = ''nix build .#checks.${pkgs.stdenv.hostPlatform.system}.module-test'';
    lint.text = ''${lib.getExe config.treefmt.build.wrapper} "$@"'';
  };

  conventional-commits.enable = true;
}
