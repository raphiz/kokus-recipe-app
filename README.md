# Kokus Recipe Management

Kokus is a simple recipe management and meal planning application. As a personal project, it serves as a playground for exploring methodologies and technologies.

The template offers a ready-to-start project skeleton and development environment including linting (), a [nix derivation](https://nix.dev/manual/nix/2.24/language/derivations) and a NixOS module (with tests!).

## Prerequisites

This project requires [Nix](https://nixos.org/) with [flakes](https://nixos.wiki/wiki/Flakes) support for the development (ktlint, detekt, sqlfluff and more), build and deployment process.

```bash
# Manually start development environment ...
nix develop
# ... or let direnv do it for you
direnv allow
```

You will be greeted with a list of all available commands.
