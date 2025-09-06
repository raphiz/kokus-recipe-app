{
  inputs,
  system,
  pkgs,
  ...
}:
inputs.git-hooks-nix.lib.${system}.run {
  src = ../../.;
  hooks = {
    alejandra.enable = true;
    convco.enable = true;

    detekt = {
      enable = true;
      name = "detekt";
      entry = let
        script = pkgs.writeShellScriptBin "detekt-wrapper" ''set -euo pipefail; IFS=','; ${pkgs.detekt}/bin/detekt --build-upon-default-config -c ${../../detekt-config.yml} --auto-correct -i "$*"; unset IFS;'';
      in "${script}/bin/detekt-wrapper";
      files = "\\.(kt|kts)$";
      excludes = ["app/fundamentals/database/src/jooq/kotlin/.*.kt"];
      language = "system";
    };
    ktlint = {
      enable = true;
      name = "ktlint";
      entry = "${pkgs.ktlint}/bin/ktlint --format";
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
}
