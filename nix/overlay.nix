{inputs, ...}: final: prev: let
  jdk = prev.temurin-bin-21;
in {
  inherit jdk;

  # Override these so we only use one JVM for all dependencies (eg ktlint, detekt, ...)
  jre_headless = jdk;
}
