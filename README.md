# Kotlin + Gradle + Nix = ❤️

This is an example starter template for your next Kotlin JVM app.

The template offers a ready-to-start project skeleton and development environment including linting (ktlint, detekt, sqlfluff and more), a [nix derivation](https://nix.dev/manual/nix/2.24/language/derivations) and a NixOS module (with tests!).

## Prerequisites

This project requires [Nix](https://nixos.org/) with [flakes](https://nixos.wiki/wiki/Flakes) support for both the development and the build process - but nothing else!

## Development Environment

```bash
# Manually start development environment ...
nix develop
# ... or let direnv do it for you
direnv allow
```

You will be greeted with a list of all available commands.

## Make the template your own

- Search and replace `Example Application` and `example` (including folder names)
- Search for `TODO` to ensure you set licenses etc. properly
- Replace the Project license
- Have fun 🚀

## Further Resources

This repo just glues the awesome work of others together.

- [blueprint](https://github.com/numtide/blueprint)
- [devshell](https://github.com/numtide/devshell)
- [pre commit](https://pre-commit.com/) and [git-hooks.nix](github.com/cachix/git-hooks.nix)
- [buildGradleApplication](https://github.com/raphiz/buildGradleApplication)
