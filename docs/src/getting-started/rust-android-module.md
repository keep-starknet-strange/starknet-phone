# Rust Modules in Android OS

This will follow the hello rust example from AOSP documentation
[here](https://source.android.com/docs/setup/build/rust/building-rust-modules/hello-rust-example)

Before following this example, make sure your system meets the minimum requirements, and
go through the AOSP setup steps [here](https://source.android.com/docs/setup/start)

use
```bash
lunch aosp_cf_x86_64_phone-trunk_staging-eng
```
for the build command

## Android Services

[Documentaiont](https://developer.android.com/develop/background-work/services)

Main idea: services are long-running operations in the background that can be accessed by multiple devices.
This is what we want for the light client, and is how Ethereum Phone implements their light client.

Must be declared in the manifest file:

```html
<manifest ... >
  ...
  <application ... >
      <service android:name=".ExampleService" />
      ...
  </application>
</manifest>
```

Services can be started or stopped by calling ```startService()``` and ```stopService()```,
this should be managed by a light client manager app.
