#!/usr/bin/env bash

set -Eeuo pipefail
IFS=$'\n\t'

NIXPKGS_REV=23d72dabcb3b12469f57b37170fcbc1789bd7457

log() { printf '[on-create] %s\n' "$*"; }
die() { printf '[on-create] ERROR: %s\n' "$*" >&2; exit 1; }

[[ -n "${WORKSPACE_DIR}" ]] || die "containerWorkspaceFolder (or WORKSPACE_DIR) is not set"

ensure_whitelist_prefix() {
  local toml="$HOME/.config/direnv/direnv.toml"
  mkdir -p "$(dirname "$toml")"

  # Avoid appending duplicates on each run.
  if [[ -f "$toml" ]] && grep -qF "\"$WORKSPACE_DIR\"" "$toml"; then
    return 0
  fi

  cat >>"$toml" <<EOF

[whitelist]
prefix = ["$WORKSPACE_DIR"]
EOF
}

ensure_line_in_file() {
  local line="$1"
  local file="$2"
  mkdir -p "$(dirname "$file")"
  touch "$file"
  grep -qxF "$line" "$file" || printf '%s\n' "$line" >>"$file"
}

install_direnv() {
  log "Configuring direnv whitelist for workspace"
  ensure_whitelist_prefix

  log "Installing direnv + nix-direnv via nix profile"
  nix profile add \
  "github:NixOS/nixpkgs/${NIXPKGS_REV}#direnv" \
  "github:NixOS/nixpkgs/${NIXPKGS_REV}#nix-direnv" \
   >/dev/null

  log "Ensuring direnvrc sources nix-direnv"
  local direnvrc="$HOME/.config/direnv/direnvrc"
  ensure_line_in_file 'source "$HOME/.nix-profile/share/nix-direnv/direnvrc"' "$direnvrc"

  log "Ensuring bash loads direnv hook"
  ensure_line_in_file 'eval "$(direnv hook bash)"' "$HOME/.bashrc"
}

build_dev_env() {
  log "Warming up nix dev environment"
  nix print-dev-env >/dev/null
}

install_direnv
build_dev_env

# Workaround for intelliJ ($XDG_CONFIG_HOME=/.jbdevcontainer/config)
sudo mkdir -p /.jbdevcontainer/config
sudo ln -s ~/.config/direnv /.jbdevcontainer/config/direnv

log "Done"
