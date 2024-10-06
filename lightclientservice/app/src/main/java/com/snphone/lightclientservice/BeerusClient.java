package com.snphone.lightclientservice;

import org.astonbitecode.j4rs.api.Instance;

public class BeerusClient {

    /**
     * @param ethExecutionRpc the Ethereum RPC Beerus will connect to
     * @param starknetRpc the Starknet RPC Beerus will connect to
     * @return
     */
    public static native String run(Instance<String> ethExecutionRpc, Instance<String> starknetRpc);

    static {
        System.loadLibrary("beerus");
    }
}
