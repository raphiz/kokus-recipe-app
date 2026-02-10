{
  pkgs,
  kokus,
  ...
}: let
  jdk = kokus.jdk;
in {
  programs = {
    alejandra.enable = true;

    ktlint = {
      enable = true;
      # Reduce closure by overriding the JDK
      package = pkgs.ktlint.override {jre_headless = jdk;};
    };

    detekt = {
      enable = true;
      configFile = ../detekt-config.yml;
      # Reduce closure by overriding the JDK
      package = pkgs.detekt.override {jre_headless = jdk;};
    };

    sqlfluff.enable = true;

    biome.enable = true;
  };
}
