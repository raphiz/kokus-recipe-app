{
  pkgs,
  flake,
  system,
  perSystem,
  ...
}: let
  kokus = flake.packages.${system}.kokus;
in
  perSystem.devshell.mkShell ({config, ...}: {
    devshell = {
      name = ''Kokus: Recipe Management'';
      startup.pre-commit.text = flake.checks.${system}.linters.shellHook;
    };

    packages = [
      kokus.jdk
      kokus.gradle
      kokus.updateVerificationMetadata
    ];

    env = [
      {
        name = "JAVA_HOME";
        value = kokus.jdk.home;
      }
    ];

    commands = [
      {
        name = "build";
        help = "compiles, runs tests, and reports success or failure";
        command = ''gradle :clean :check :assemble'';
      }
      {
        name = "build-continuously";
        help = "automatically run build when files change";
        command = ''gradle --continuous :check :assemble'';
      }
      {
        name = "rundev";
        help = "run the software locally for manual review and testing";
        command = ''gradle :run'';
      }
      {
        name = "module-test";
        help = "run module integration tests in a production-like environment";
        command = "nix build .#checks.${system}.module-test";
      }
      {
        name = "lint";
        help = "run all linters - or specific ones when passed as arguments";
        command = ''pre-commit run --all-files "''${@}"'';
      }
    ];
  })
