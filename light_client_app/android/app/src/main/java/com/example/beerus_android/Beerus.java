package com.example.beerus_android;

public class Beerus {
    public static native Void run(
        String eth_execution_rpc,
        String starknet_rpc,
        String data_dir,
        int poll_secs,
        int socket_port
    );

    static {
        // here we load the rust library we created
        System.loadLibrary("beerus");
    }
}
