package com.snphone.lightclientservice;

public class BeerusClient {

    /**
     * @param ethExecutionRpc
     * @param starknetRpc
     * @return
     */
    public static native String run(String ethExecutionRpc, String starknetRpc);

    static {
        System.loadLibrary("beerus");
    }

}
