# AndroidX AppFunctions extension API compile stubs

These four Java files are copied unchanged from the AndroidX Support repository
at commit `0990e6ecc6b2bb46ca9e51bdc845da764080ba4f`:

`https://android.googlesource.com/platform/frameworks/support/+/0990e6ecc6b2bb46ca9e51bdc845da764080ba4f/appfunctions/appfunctions-stubs/src/main/java/com/android/extensions/appfunctions/`

They retain the upstream Apache License 2.0 headers. They are **compile-only
stubs**: every API body throws `RuntimeException("Stub!")`. They are not an
implementation and must never be packaged in an APK or used at runtime. Runtime
implementations are supplied by the Android AppFunctions platform/extension
libraries.

The full Apache License 2.0 text is included in [`LICENSE.txt`](LICENSE.txt).
The pinned AndroidX repository has no root `LICENSE.txt`, so this is the
official license text from `https://www.apache.org/licenses/LICENSE-2.0.txt`.

SHA-256 checksums of the vendored source files:

| File | SHA-256 |
| --- | --- |
| `AppFunctionException.java` | `f544f198700c0b061add9a2ccb7cf55946d050891eb90e2c7c81148425a0a0a0` |
| `AppFunctionService.java` | `bae4711f129ca422350958178e42b183565fb3d479a3fe3144d605497b3e8dad` |
| `ExecuteAppFunctionRequest.java` | `7e6849d233737b4d091651702a72ae1d43e623ebf9aa35348ad565deacaaff9a` |
| `ExecuteAppFunctionResponse.java` | `81263b2f7b3ec30b010c2cce1cf2784b84e984683897300c46a62e3c64d273ed` |

The import closure is Android SDK APIs (`android.app.Service`, `android.app.appsearch.GenericDocument`, `android.content.Intent`, and `android.os.*`), AndroidX annotations (`androidx.annotation.*`), and JSpecify (`org.jspecify.annotations.*`), plus standard Java APIs. This source set therefore requires Android SDK, `androidx.annotation`, and `jspecify` on its compile classpath.
