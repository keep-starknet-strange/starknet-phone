package com.example.walletapp.data.datasource;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001JD\u0010\u0002\u001a$\u0012\u001c\u0012\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00040\u00040\u0003j\u0002`\u00072\b\b\u0001\u0010\b\u001a\u00020\u00052\b\b\u0001\u0010\t\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/example/walletapp/data/datasource/CoinGeckoApi;", "", "getTokenPrices", "Lretrofit2/Response;", "", "", "", "Lcom/example/walletapp/data/datasource/GetTokenPriceResponse;", "ids", "vsCurrencies", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface CoinGeckoApi {
    
    @retrofit2.http.Headers(value = {"accept: application/json", "x-cg-demo-api-key: CG-mRdWfNFoZnKVan4GNdTrhZjL"})
    @retrofit2.http.GET(value = "simple/price")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTokenPrices(@retrofit2.http.Query(value = "ids")
    @org.jetbrains.annotations.NotNull()
    java.lang.String ids, @retrofit2.http.Query(value = "vs_currencies")
    @org.jetbrains.annotations.NotNull()
    java.lang.String vsCurrencies, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Double>>>> $completion);
}