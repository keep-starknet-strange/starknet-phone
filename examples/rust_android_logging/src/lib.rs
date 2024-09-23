use android_logger::Config;
use jni::JNIEnv;
use jni::objects::{JClass, JString};
use log::{Level, info, error, warn, debug, trace};

#[no_mangle]
pub extern "C" fn Java_com_example_RustLogger_initLogger(mut _env: JNIEnv, _class: JClass) {
    android_logger::init_once(
        Config::default()
            .with_min_level(Level::Trace)
            .with_tag("RustLogger")
    );
    info!("Rust logger initialized");
}

#[no_mangle]
pub extern "C" fn Java_com_example_RustLogger_logInfo(mut _env: JNIEnv, _class: JClass, msg: jni::sys::jstring) {
    let msg_jstring = unsafe { JString::from_raw(msg) };
    let msg: String = _env.get_string(&msg_jstring).expect("Couldn't get java string!").into();
    info!("{}", msg);
}

#[no_mangle]
pub extern "C" fn Java_com_example_RustLogger_logError(mut _env: JNIEnv, _class: JClass, msg: jni::sys::jstring) {
    let msg_jstring = unsafe { JString::from_raw(msg) };
    let msg: String = _env.get_string(&msg_jstring).expect("Couldn't get java string!").into();
    error!("{}", msg);
}

#[no_mangle]
pub extern "C" fn Java_com_example_RustLogger_logWarn(mut _env: JNIEnv, _class: JClass, msg: jni::sys::jstring) {
    let msg_jstring = unsafe { JString::from_raw(msg) };
    let msg: String = _env.get_string(&msg_jstring).expect("Couldn't get java string!").into();
    warn!("{}", msg);
}

#[no_mangle]
pub extern "C" fn Java_com_example_RustLogger_logDebug(mut _env: JNIEnv, _class: JClass, msg: jni::sys::jstring) {
    let msg_jstring = unsafe { JString::from_raw(msg) };
    let msg: String = _env.get_string(&msg_jstring).expect("Couldn't get java string!").into();
    debug!("{}", msg);
}

#[no_mangle]
pub extern "C" fn Java_com_example_RustLogger_logTrace(mut _env: JNIEnv, _class: JClass, msg: jni::sys::jstring) {
    let msg_jstring = unsafe { JString::from_raw(msg) };
    let msg: String = _env.get_string(&msg_jstring).expect("Couldn't get java string!").into();
    trace!("{}", msg);
}