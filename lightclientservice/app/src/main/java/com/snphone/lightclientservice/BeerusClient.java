package com.snphone.lightclientservice;

public class BeerusClient {

    /**
     * @param ethExecutionRpc the Ethereum RPC Beerus will connect to
     * @param starknetRpc the Starknet RPC Beerus will connect to
     * @return
     */
    public static native String run(String ethExecutionRpc, String starknetRpc, String dataDir);

    /**
     * just used to test communication between rust and android side
     * @return the same string as inputted
     */
    public static native String echo(String message);

    /**
     * just used to test communication between rust and android side, blocks while
     * waiting for async return.
     * @return the same string as inputted
     */
    public static native String echoBlock(String message);

    static {
        System.loadLibrary("beerus");
    }
}
