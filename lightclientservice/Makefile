build-android:
	cargo ndk -t x86_64 -p 21 build --release

update-android:
	cp target/x86_64-linux-android/release/libbeerus.so app/src/main/jniLibs/x86_64/
