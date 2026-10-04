# Native Rust boundary

Own the experimental cdylib/JNI surface. Preserve exported symbol names and JVM signature compatibility with any actual caller; do not claim Android loading merely from a Cargo build declaration. Keep native outputs out of source edits. This is not a registered Android module and has no permitted testDebugUnitTest target; native verification needs explicit scope.
