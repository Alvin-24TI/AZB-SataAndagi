package com.example.azb_sataandagi.model

import com.example.azb_sataandagi.R
import com.google.gson.annotations.SerializedName

data class MenuItem(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    val imageRes: Int = R.drawable.satandagi
)
