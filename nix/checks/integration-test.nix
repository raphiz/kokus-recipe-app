{
  system,
  flake,
  pkgs,
  ...
}:
pkgs.nixosTest (
  {...}: let
    serverDomain = "example.local";
    serverPort = 4242;
  in {
    name = "Example Application integration test";
    nodes = {
      server = {...}: {
        imports = [flake.nixosModules.example];

        services.example = {
          enable = true;
          package = flake.packages.${system}.example;
          port = serverPort;
        };

        networking.hosts."::1" = ["${serverDomain}"];
        networking.firewall.allowedTCPPorts = [serverPort];
      };

      client = {nodes, ...}: {
        networking.hosts."${nodes.server.networking.primaryIPAddress}" = ["${serverDomain}"];
      };
    };

    testScript = ''
      start_all()
      server.wait_for_unit("example.service")
      server.wait_until_succeeds("curl -f -L http://${serverDomain}:${toString serverPort}/")
    '';
  }
)
