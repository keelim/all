modules=("app-arducon" "app-cnubus" "app-comssa" "app-my-grade" "app-nanda")

# Loop through each module
MSG=""
for module in "${modules[@]}"; do
    MSG=$MSG" $module:compileDebugSources"
done

./gradlew $MSG
