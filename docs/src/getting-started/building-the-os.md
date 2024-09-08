# Building the OS

## Build Dependencies

Baseline build dependencies:

- x86_64 Linux build environment.
- 32GiB of memory or more. Link-Time Optimization (LTO) creates huge peaks
  during linking and is mandatory for Control Flow Integrity (CFI). Linking
  Vanadium (Chromium) and the Linux kernel with LTO + CFI are the most memory
  demanding tasks.
- 100GiB+ of additional free storage space for a typical build of the entire
  OS for a multiarch device.

Setup for Debiand GNU/Linux 12 (bookworm) based systems:

```bash
sudo apt update
sudo apt install repo yarnpkg zip rsync
echo 'export PATH=$PATH:/sbin:/usr/sbin:/usr/local/sbin' >> ~/.bashrc
source ~/.bashrc
```

More detailed dependiencies and build guide can be found
[here](https://grapheneos.org/build)

## Downloading the source code

### Development Branch

Starknet Phone forks the '14' branch of grapheneOS, which is the main
development branch of GrapheneOS.

```bash
mkdir starknet-phone-os
cd starknet-phone-os
repo init -u \
https://github.com/suffix-labs/snphone_platform_manifest -b 14
repo sync -j8
```

### Emulator builds

NOTE: must be done from bash or zsh

set up build environment

```bash
source build/envsetup.sh
```

set the build target

```bash
lunch sdk_phone64_x86_64-cur-eng
```

start the build. This can take multiple hours to run.

```bash
m
```

### Adding prebuilt binaries

sync repo

```bash
mkdir -p android/kernel/6.1
cd android/kernel/6.1
repo init -u https://github.com/GrapheneOS/kernel_manifest-6.1.git -b 14
repo sync -j8
```

build the kernel image and modules for the emulator

```bash
ARCH=x86_64 common/build_virt.sh
```

replace the prebuilts in the OS source tree

```bash
ANDROID_BUILD_TOP=~/starknet-phone-os ARCH=x86_64 common/update_virt_prebuilts.sh
```
