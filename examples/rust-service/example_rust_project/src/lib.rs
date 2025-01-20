mod android;

pub fn hello(input: &str) -> String {
    format!("Hello, {}", input)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_works() {
        let input: &str = "Test";
        let result = hello(input);
        assert_eq!(result, "Hello, Test")
    }
}
