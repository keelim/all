use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::jstring;

#[unsafe(no_mangle)]
pub extern "C" fn Java_com_keelim_all_bridge(env: JNIEnv, _: JClass, _: JString) -> jstring {
    let output = "Hello World".to_string();

    env.new_string(output)
        .expect("Failed to create Java string")
        .as_raw()
}
