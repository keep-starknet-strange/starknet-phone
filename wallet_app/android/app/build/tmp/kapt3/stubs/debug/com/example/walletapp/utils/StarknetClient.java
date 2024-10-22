package com.example.walletapp.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0010R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0003X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/example/walletapp/utils/StarknetClient;", "", "rpcUrl", "", "(Ljava/lang/String;)V", "accountAddress", "keystore", "Lcom/example/walletapp/utils/Keystore;", "privateKey", "provider", "Lcom/swmansion/starknet/provider/rpc/JsonRpcProvider;", "tag", "deployAccount", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEthBalance", "Lcom/swmansion/starknet/data/types/Uint256;", "Lcom/swmansion/starknet/data/types/Felt;", "(Lcom/swmansion/starknet/data/types/Felt;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendERC20", "weiToEther", "Ljava/math/BigDecimal;", "wei", "app_debug"})
public final class StarknetClient {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String rpcUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final com.swmansion.starknet.provider.rpc.JsonRpcProvider provider = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String privateKey = "null";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String accountAddress = "null";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tag = "StarknetClient";
    @org.jetbrains.annotations.NotNull()
    private final com.example.walletapp.utils.Keystore keystore = null;
    
    public StarknetClient(@org.jetbrains.annotations.NotNull()
    java.lang.String rpcUrl) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deployAccount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEthBalance(@org.jetbrains.annotations.NotNull()
    com.swmansion.starknet.data.types.Felt accountAddress, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.swmansion.starknet.data.types.Uint256> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendERC20(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.math.BigDecimal weiToEther(@org.jetbrains.annotations.NotNull()
    com.swmansion.starknet.data.types.Uint256 wei) {
        return null;
    }
}