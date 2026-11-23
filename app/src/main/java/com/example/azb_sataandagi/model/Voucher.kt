package com.example.azb_sataandagi.model

import com.google.gson.annotations.SerializedName

data class Voucher(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("point_cost")
    val pointCost: Int,

    @SerializedName("voucher_code")
    val voucherCode: String,

    @SerializedName("is_claimed")
    var isClaimed: Boolean = false
)
