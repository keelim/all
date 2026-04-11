#!/usr/bin/env bash
set -euo pipefail

require_command() {
    local command_name="$1"

    if ! command -v "$command_name" >/dev/null 2>&1; then
        printf 'Error: required command "%s" was not found in PATH.\n' "$command_name" >&2
        exit 1
    fi
}

discover_modules() {
    ./gradlew tasks --all |
        sed -n 's/^\([^[:space:]][^[:space:]]*\):generateModulesGraphvizText$/:\1/p' |
        sort -u
}

require_command "dot"
require_command "svgo"

if [[ ! -x "./gradlew" ]]; then
    printf 'Error: required executable "./gradlew" was not found.\n' >&2
    exit 1
fi

modules=()
while IFS= read -r module; do
    modules+=("${module}")
done < <(discover_modules)

if [[ "${#modules[@]}" -eq 0 ]]; then
    printf 'Error: no modules exposing generateModulesGraphvizText were found.\n' >&2
    exit 1
fi

for gradle_module in "${modules[@]}"; do
    module_name="${gradle_module#:}"
    output_dir="./$(printf '%s' "${module_name}" | tr ':' '/')"
    gv_file="${module_name}.gv"
    svg_file="${output_dir}/${module_name}.svg"

    mkdir -p "$output_dir"

    ./gradlew \
        --rerun-tasks \
        "${gradle_module}:generateModulesGraphvizText" \
        -Pmodules.graph.output.gv="${gv_file}" \
        -Pmodules.graph.of.module="${gradle_module}"

    if [[ ! -s "${gv_file}" ]]; then
        printf 'Error: expected Graphviz output "%s" for module "%s" was not created or is empty.\n' \
            "${gv_file}" \
            "${gradle_module}" >&2
        exit 1
    fi

    dot -Tsvg "${gv_file}" |
        svgo --multipass --pretty --output="${svg_file}" -

    rm -f "${gv_file}"
done
