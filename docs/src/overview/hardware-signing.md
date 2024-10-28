

# Leveraging secure enclave for hardware signing


## The Android side 

Android allows for creating and signing with a secure private key that cannot be 
extracted from the device via the secure keystore system. 

[Android Keystore Documentation](https://developer.android.com/privacy-and-security/keystore)

Some notable features: 
- Extraction prevention: key material cannot be extracted from the deivce in case of theft and 
   cannot be accessed by application processes.
- Hardware security module: similar to a Trusted Execution Environment (TEE), but specifically 
   embedded Secure Elements (eSE) or on-SoC secure processing units (iSE)
- Key use authorizations: specific authorizations can be set (and cannot be changed) to enforce 
   key usage to meet certain constraints such as specific algortithms or time intervals.


## The Starknet side 

On Starknet, our account implementation needs to allow for this hardware signer.

