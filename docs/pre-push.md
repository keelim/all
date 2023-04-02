# Pre-push

## The pre-push rule follows the following steps:

1. Create a .git/hooks/pre-push file.
2. Write the shell script below.

    ```shell

    #!/bin/sh
    FILES=$(git diff --cached --name-only --diff-filter=AM | grep -E '\.kt$|\.kts$|\.xml$')

    if [ -z "$FILES" ]; then
    exit 0
    fi

    for FILE in $FILES; do
      ./gradlew --init-script gradle/init.gradle.kts spotlessApply -Pspotless.target="$FILE"
    done

    ```

3. Perform a push operation to verify that the above steps are applied.
