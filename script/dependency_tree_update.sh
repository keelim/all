modules=("app-arducon" "app-cnubus" "app-comssa" "app-my-grade" "app-nanda")

# Loop through each module
for module in "${modules[@]}"; do
    ./gradlew "$module":dependencyGuardBaseline
done
