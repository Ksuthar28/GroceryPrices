package com.sample.marketprices.models

import com.google.gson.annotations.SerializedName

data class MarketResponse(
    @SerializedName("records")
    val result: MutableList<Market>,
    @SerializedName("total")
    val totalResults: Int,
    @SerializedName("error")
    val error: String
)

data class ResError(
    @SerializedName("code")
    val code: String,
    @SerializedName("description")
    val description: String
)