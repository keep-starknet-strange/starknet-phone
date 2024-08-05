//#[cfg(target_os = "android")]
//#[path = "android.rs"]
mod android;

pub fn hello() -> String {
    let msg = "Hello from Rust!";
    msg.to_string()
}
