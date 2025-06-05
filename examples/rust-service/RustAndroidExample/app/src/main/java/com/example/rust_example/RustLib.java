package com.example.rust_example;

public class RustLib {

    // define the interface for using the rust library below
    public static native String hello(String input);

    static {
        // here we load the rust library we created
        System.loadLibrary("example_rust_project");
    }
}
