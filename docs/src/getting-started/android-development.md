
# Android Development Crash Course 

Because GrapheneOS (and thus Starknet Phone) is based on android, it is compatible 
with Android native applications. This will be a broad overview of how Android 
development is approached in this project. 

## Environment/Android Studio setup

Android applications are best developed in Android Studio or Intellij. This will 
focus on Android Studio.

[Install Android Studio](https://developer.android.com/studio)

## Hello World 

Below is a full intro course on building a simple android app 

[First Android App codelab](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app#0)

## A More Complicated Example 

This project will place a strong emphasis on Jetpack Compose. 

[Jetpack Compose Docs](https://developer.android.com/develop/ui/compose/documentation)

Android provides number of examples using this approach, of which we will be 
following the decisions of the JetNews example linked below. 

[JetNews Example Application](https://github.com/android/compose-samples/tree/main/JetNews)

## Using Rust 

In some cases (like the Beerus client) we will not be able to use Kotlin for everything in an 
application. For this, follow the [rust in android guide](./rust-in-android.md)
