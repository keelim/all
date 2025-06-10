modules=("app-arducon" "app-cnubus" "app-comssa" "app-my-grade" "app-nanda")
tasks=""

# Loop through each module
for module in "${modules[@]}"; do
    tasks+=":$module:dependencyGuardBaseline "
done
./gradlew $tasks
