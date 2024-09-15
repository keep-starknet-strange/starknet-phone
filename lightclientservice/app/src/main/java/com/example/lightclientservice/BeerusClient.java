package com.example.lightclientservice;

public class BeerusClient {

    public static native String run(String ethExecutionRpc, String starknetRpc);

    static {
        System.loadLibrary("beerus");
    }

}
