./gradlew clean

modules=(
    "app-arducon"
    "app-cnubus"
    "app-my-grade"
    "app-nanda"
    "app-comssa"
    "core:common"
    "core:common-android"
    "core:compose-core"
    "core:data"
    "core:data-api"
    "core:database"
    "core:datastore-proto"
    "core:domain"
    "core:model"
    "core:network"
    "core:navigation"
    "feature:ui-labs"
    "feature:ui-setting"
    "feature:ui-web"
    "shared"
)


# Loop through each module
for module in "${modules[@]}"; do
    # Convert Graphviz file to SVG
    ./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv="$module".gv -Pmodules.graph.of.module=:"$module"

    # `module` 에서 ":" 를 포함하면 이름을 split 해서 따로 변수 넣어줘
    if [[ "$module" == *":"* ]]; then
            IFS=':' read -r part1 part2 <<< "$module"
            output_dir="./${part1}/${part2}"
        else
            output_dir="./${module}"
    fi

    dot -Tsvg "$module.gv" |
              svgo --multipass --pretty --output="${output_dir}/${module}.svg" -

    # Remove the Graphviz file if it exists
    if [ -f "$module.gv" ]; then
        rm "$module.gv"
    fi
done
