{
  system,
  flake,
  pkgs,
  ...
}:
pkgs.testers.nixosTest (
  {...}: let
    serverDomain = "kokus.local";
    serverPort = 4242;
  in {
    name = "Kokus NixOS Module Test";
    nodes = {
      server = {...}: {
        imports = [flake.nixosModules.kokus];

        services.kokus = {
          enable = true;
          package = flake.packages.${system}.kokus;
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
      server.wait_for_unit("kokus.service")
      server.wait_until_succeeds("curl -f -L http://${serverDomain}:${toString serverPort}/")
    '';
  }
)
