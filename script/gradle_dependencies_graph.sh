#!/usr/bin/env bash
set -euo pipefail

repo_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_dir"

if [[ ! -x ./gradlew ]]; then
    printf 'Error: required executable "./gradlew" was not found.\n' >&2
    exit 1
fi

# Configure the entire project before collecting declared project dependencies.
# Pass -PmoduleGraph.module=:app-arducon to select its reachable subgraph.
exec ./gradlew --no-configure-on-demand generateModuleDependencyGraph "$@"
