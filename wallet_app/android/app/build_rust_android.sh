#!/bin/bash

RUST_PROJECT_PATH="../../examples/rust_android_logging"
JNI_LIBS_PATH="./src/main/jniLibs"

# Ensure the jniLibs directory exists
mkdir -p $JNI_LIBS_PATH

# Build for different Android architectures
cargo ndk -t armeabi-v7a -o $JNI_LIBS_PATH build --release --manifest-path=$RUST_PROJECT_PATH/Cargo.toml
cargo ndk -t arm64-v8a -o $JNI_LIBS_PATH build --release --manifest-path=$RUST_PROJECT_PATH/Cargo.toml
cargo ndk -t x86 -o $JNI_LIBS_PATH build --release --manifest-path=$RUST_PROJECT_PATH/Cargo.toml
cargo ndk -t x86_64 -o $JNI_LIBS_PATH build --release --manifest-path=$RUST_PROJECT_PATH/Cargo.toml
