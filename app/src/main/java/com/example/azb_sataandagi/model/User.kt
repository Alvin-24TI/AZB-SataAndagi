package com.example.azb_sataandagi.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String? = null,
    
    @SerializedName("nama")
    val nama: String,
    
    @SerializedName("phone")
    val phone: String,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("created_at")
    val createdAt: String? = null
)
