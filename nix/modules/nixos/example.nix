{
  config,
  lib,
  pkgs,
  ...
}: let
  cfg = config.services.example;
in {
  options = {
    services.example = {
      enable = lib.mkEnableOption "Enable Example Application";

      package = lib.mkOption {
        type = lib.types.package;
        default = pkgs.example;
        description = "Application package to use.";
      };

      user = lib.mkOption {
        type = lib.types.str;
        default = "example";
        description = "User account under which Example Application runs";
      };

      port = lib.mkOption {
        type = lib.types.port;
        default = 8000;
        description = "The port on which Example Application listens.";
      };
    };
  };

  config = lib.mkIf cfg.enable {
    users.users.example =
      lib.mkIf (cfg.user == "example")
      {
        name = "example";
        isSystemUser = true;
        group = "example";
        description = "Example Application server user";
      };
    users.groups.example = lib.mkIf (cfg.user == "example") {};

    environment.systemPackages = [cfg.package];

    systemd.services.example = {
      description = "Example Application";

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
}
