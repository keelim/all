modules=("app-arducon" "app-cnubus" "app-comssa" "app-my-grade" "app-nanda")

# Loop through each module
for module in "${modules[@]}"; do
    # Convert Graphviz file to SVG
    ./gradlew generateModulesGraphvizText -Pmodules.graph.output.gv="$module".gv -Pmodules.graph.of.module=:"$module"

    dot -Tsvg "$module.gv" |
          svgo --multipass --pretty --output="./${module}/${module}.svg" -

    # Remove the Graphviz file if it exists
    if [ -f "$module.gv" ]; then
        rm "$module.gv"
    fi
done
