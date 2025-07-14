{
  pkgs,
  flake,
  system,
  perSystem,
  ...
}:
perSystem.devshell.mkShell ({config, ...}: {
  devshell = {
    name = ''Example Application'';
    startup.pre-commit.text = ''if [ -z "''${CI:-}" ]; then ${flake.checks.${system}.linters.shellHook} fi'';
  };

  packages =
    [
      perSystem.build-gradle-application.updateVerificationMetadata
    ]
    ++ flake.packages.${system}.default.nativeBuildInputs;

  commands = [
    {
      name = "build";
      help = "compiles, runs tests, and reports success or failure";
      command = ''gradle :clean :check :installDist'';
    }
    {
      name = "build-continuously";
      help = "automatically run build when files change";
      command = ''gradle --continuous :check :installDist'';
    }
    {
      name = "rundev";
      help = "run the software locally for manual review and testing";
      command = ''gradle :run'';
    }
    {
      name = "integration-test";
      help = "run integration tests in a production-like environment";
      command = "nix build .#checks.${system}.integrationTest";
    }
    {
      name = "lint";
      help = "run all linters - or specific ones when passed as arguments";
      command = ''pre-commit run --all-files "''${@}"'';
    }
  ];
})
