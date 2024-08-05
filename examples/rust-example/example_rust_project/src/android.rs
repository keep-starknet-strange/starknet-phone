use jni::{
    objects::{JClass, JString},
    sys::jstring,
    JNIEnv,
};

#[no_mangle]
pub extern "system" fn Java_com_example_rust_example_RustLib_hello<'local>(
    mut env: JNIEnv<'local>, // this is the class that owns our static method
    _class: JClass<'local>,
    input: JString<'local>,
) -> jstring {
    // getting the input string from our android app out of java
    let input: String = env
        .get_string(&input)
        .expect("Couldn't get java string!")
        .into();

    // creating a new java string to return to our android app
    let output = env
        .new_string(format!("Hello, {}!", input))
        .expect("Couldn't create a java string!");

    output.into_raw()
}
