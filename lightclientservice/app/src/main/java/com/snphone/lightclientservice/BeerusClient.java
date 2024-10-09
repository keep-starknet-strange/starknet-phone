package com.snphone.lightclientservice;

import org.astonbitecode.j4rs.api.Instance;

public class BeerusClient {

    /**
     * @param ethExecutionRpc the Ethereum RPC Beerus will connect to
     * @param starknetRpc the Starknet RPC Beerus will connect to
     * @return
     */
    public static native String run(Instance<String> ethExecutionRpc, Instance<String> starknetRpc);

    /**
     * just used to test communication between rust and android side
     * @return the same string as inputted
     */
    public static native String echo(Instance<String> message);

    static {
        System.loadLibrary("beerus");
    }
}
