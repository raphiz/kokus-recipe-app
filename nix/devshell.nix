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
