# devshell configured via https://github.com/ergon/nix-shell-parts
{
  lib,
  config,
  pkgs,
  system,
  ...
}: let
  kokus = config.packages.kokus;
  jdk = kokus.jdk;
  gradle = kokus.gradle;
in {
  shells.default = {config, ...}: {
    packages = [
      kokus.updateVerificationMetadata
      gradle
      jdk
    ];

    env. "JAVA_HOME" = jdk.home;

    treefmt = {
      enable = true;
      pre-commit-hook = true;

      programs = {
        alejandra.enable = true;

        ktlint = {
          enable = true;
          # Reduce closure by overriding the JDK
          package = pkgs.ktlint.override {jre_headless = jdk;};
        };

        detekt = {
          enable = true;
          configFile = ../../detekt-config.yml;
          # Reduce closure by overriding the JDK
          package = pkgs.detekt.override {jre_headless = jdk;};
        };

        # TODO: convco

        sqlfluff.enable = true;

        biome.enable = true;
      };
    };

    scripts = {
      build.text = ''${lib.getExe gradle} :clean :check :assemble'';
      build-continuously.text = ''${lib.getExe gradle} --continuous :check :assemble'';
      rundev.text = ''${lib.getExe gradle} :run'';
      module-test.text = ''nix build .#checks.${system}.module-test'';
      lint.text = ''${lib.getExe config.treefmt.build.wrapper} "$@"'';
    };
  };
}
