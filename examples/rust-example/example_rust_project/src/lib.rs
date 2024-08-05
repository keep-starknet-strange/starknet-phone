use core::ffi::c_char;
use std::ffi::CString;

#[no_mangle]
pub unsafe extern "C" fn rust_hello() -> *const c_char {
    let msg = CString::new("Hello, Android from Rust!").unwrap();
    msg.into_raw()
}
