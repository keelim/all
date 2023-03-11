# Pre-push

## The pre-push rule follows the following steps:

1. Create a .git/hooks/pre-push file.
2. Write the shell script below.

    ```shell
    #!/bin/sh

    ./gradlew --init-script gradle/init.gradle.kts spotlessApply

    ```

3. Perform a push operation to verify that the above steps are applied.
