{config, ...}: {
  flake.nixosModules = {
    default = config.nixosModules.kokus;
    kokus = {
      config,
      lib,
      pkgs,
      ...
    }: let
      cfg = config.services.kokus;
    in {
      options = {
        services.kokus = {
          enable = lib.mkEnableOption "Kokus Recipe Management";

          package = lib.mkOption {
            type = lib.types.package;
            default = pkgs.kokus;
            description = "Application package to use.";
          };

          user = lib.mkOption {
            type = lib.types.str;
            default = "kokus";
            description = "User account under which Kokus runs";
          };

          port = lib.mkOption {
            type = lib.types.port;
            default = 8000;
            description = "The port on which Kokus listens.";
          };
        };
      };

      config = lib.mkIf cfg.enable {
        users.users.kokus =
          lib.mkIf (cfg.user == "kokus")
          {
            name = "kokus";
            isSystemUser = true;
            group = "kokus";
            description = "Kokus server user";
          };
        users.groups.kokus = lib.mkIf (cfg.user == "kokus") {};

        environment.systemPackages = [cfg.package];

        systemd.services.kokus = {
          description = "Kokus Recipe Management";

          wantedBy = ["multi-user.target"];
          after = ["network.target"];
          requires = ["nss-lookup.target" "network-online.target"];
          serviceConfig = {
            ExecStart = lib.getExe cfg.package;
            User = cfg.user;
            Restart = "always";
            RestartSec = "5s";
            Environment = [
              ''SERVER_PORT=${toString cfg.port}''
            ];
          };
        };
      };
    };
  };
}
