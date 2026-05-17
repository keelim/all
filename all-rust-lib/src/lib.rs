use jni::EnvUnowned;
use jni::objects::{JClass, JString};
use jni::sys::jstring;

#[unsafe(no_mangle)]
pub extern "system" fn Java_com_keelim_all_bridge<'local>(
    mut env: EnvUnowned<'local>,
    _: JClass<'local>,
    _: JString<'local>,
) -> jstring {
    env.with_env(|env| -> jni::errors::Result<jstring> {
        Ok(env.new_string("Hello World")?.into_raw())
    })
    .resolve::<jni::errors::ThrowRuntimeExAndDefault>()
}
