package com.example.azb_sataandagi.model

data class Voucher(
    val id: String,
    val title: String,
    val description: String,
    val pointCost: Int,
    val voucherCode: String,
    var isClaimed: Boolean = false
)
