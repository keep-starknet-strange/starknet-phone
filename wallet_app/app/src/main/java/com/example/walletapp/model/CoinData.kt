package com.example.walletapp.model

import com.google.gson.annotations.SerializedName

data class CoinData(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("web_slug") val webSlug: String,
    @SerializedName("asset_platform_id") val assetPlatformId: String,
    @SerializedName("image") val image: ImageData,
    @SerializedName("contract_address") val contractAddress: String
)

data class ImageData(
    @SerializedName("thumb") val thumb: String,
    @SerializedName("small") val small: String,
    @SerializedName("large") val large: String
)
