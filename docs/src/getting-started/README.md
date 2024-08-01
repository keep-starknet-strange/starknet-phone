# Getting Started

## Install Android Studio

Most development, at least starting out, will be on the 3
android applications that form the core of the starknet
phone: light client, wallet, and browser.

1. Install [Android Studio](https://developer.android.com/studio/install).
1. Create a new pixel device with the latest android SDK. Follow the instructions
   [here](https://developer.android.com/studio/run/managing-avds)
1. Create a fork of this repository, and open the application in Android Studio.
1. Refer to the [contribution guide](<>) for any contributions.

## Installing on an emulator

NOTE: We are still working to provide an image for download. For now,
development on any of the applications can be done on a generic android OS.

Steps to install on an emulator:

1. Download a zip file of the image.
1. Make sure [Android Studio](https://developer.android.com/studio/install) is installed.
1. Navigate to the Android SDK install location. On mac this default to `Library/Android/sdk`

```bash
cd Library/Android/sdk
```

4. Create a new directory called "android-32", and inside that directory create
   a new directory called "default"

```bash
mkdir android-32/default
```

5. Extract the zip file of the OS image to the newly created "default" directory
