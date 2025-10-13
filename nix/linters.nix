{
  perSystem = {
    pkgs,
    lib,
    config,
    ...
  }: let
    # use a single JDK (as JRE) to reduce closure size
    jdk = config.packages.kokus.jdk;
  in {
    pre-commit.settings = {
      package = pkgs.prek;
      src = ../.;

      hooks = {
        alejandra.enable = true;
        convco.enable = true;

        detekt = let
          detekt = pkgs.detekt.override {
            jre_headless = jdk;
          };
          #   detektWrapper = pkgs.writeShellScriptBin "detekt-wrapper" ''set -euo pipefail; IFS=','; ${lib.getExe detekt} --build-upon-default-config -c ${../detekt-config.yml} --auto-correct -i "$*"; unset IFS;'';
          detektWrapper = pkgs.writeShellApplication {
            name = "detekt-wrapper";
            text = ''
              # join all file args with commas for detekt's -i
              inputs="$(
                ( IFS=,; echo "$*" )
              )"

              exec ${lib.getExe detekt} \
                --build-upon-default-config \
                -c ${../detekt-config.yml} \
                --auto-correct \
                -i "$inputs"
            '';
          };
        in {
          enable = true;
          name = "detekt";
          entry = lib.getExe detektWrapper;
          files = "\\.(kt|kts)$";
          excludes = ["app/fundamentals/database/src/jooq/kotlin/.*.kt"];
          language = "system";
        };

        ktlint = let
          ktlint = pkgs.ktlint.override {
            jre_headless = jdk;
          };
        in {
          enable = true;
          name = "ktlint";
          entry = "${lib.getExe ktlint} --format";
          files = "\\.(kt|kts)$";
          language = "system";
        };

        sqlfluff = {
          enable = true;
          name = "sqlfluff";
          entry = "${pkgs.sqlfluff}/bin/sqlfluff fix";
          files = "\\.sql$";
          language = "system";
        };

        biome = {
          enable = true;
          name = "biome";
          entry = "${pkgs.biome}/bin/biome check --write --use-editorconfig=true --diagnostic-level=warn";
          files = "\\.(js|ts|jsx|tsx|json|html|css)$";
          language = "system";
        };
      };
    };
  };
}
