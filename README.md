# Kokus Recipe Management

Kokus is a simple recipe management and meal planning application. As a personal project, it serves as a playground for exploring methodologies and technologies.

## Highlights

This project is intentionally a little *over-engineered* – but in a fun way. It showcases a modern, modular setup that integrates many moving parts into a cohesive whole:

### Architecture

* **Modular Monolith** – domain-oriented structure with a non-cyclic module graph.
* **Hexagonal architecture per module** – strict classpath separation per layer.
* **Auto-generated system diagrams** – C4 system context and container diagrams are kept up to date automatically.

### Development & Tooling

* **Reproducible environment** – fully modular, Nix-based development setup and CI pipeline.
* **Automated code quality checks** – ktlint, detekt, sqlfluff and more, enforced via CI and pre-commit hooks.
* **Modern Gradle build** – parallel, cacheable, modular with convention plugins, version catalogs and more.

### Testing

* **Production-like integration tests** – full VM (NixOS test) to run module integration in realistic environments.

### Automation & Distribution

* **Nix integration** – project exposed as both a native Nix package and a NixOS module via flakes.

## Development Setup

This project requires [Nix](https://nixos.org/) with [flakes](https://nixos.wiki/wiki/Flakes) support for the development, build and deployment process.

```bash
# Manually start development environment ...
nix develop
# ... or let direnv do it for you
direnv allow
```

You will be greeted with a list of all available commands.
