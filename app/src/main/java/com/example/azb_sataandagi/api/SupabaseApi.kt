package com.example.azb_sataandagi.api

import com.example.azb_sataandagi.model.MenuItem
import com.example.azb_sataandagi.model.User
import com.example.azb_sataandagi.model.Voucher
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApi {

    // --- USERS ---
    @GET("users")
    suspend fun getUsers(
        @Query("username") usernameQuery: String? = null
    ): Response<List<User>>

    @POST("users")
    suspend fun insertUser(
        @Body user: User
    ): Response<List<User>>

    // --- TOPIK 1: MENU ITEMS (CRUD) ---
    @GET("menu_items")
    suspend fun getMenuItems(
        @Query("select") select: String = "*",
        @Query("order") order: String = "id.asc"
    ): Response<List<MenuItem>>

    @POST("menu_items")
    suspend fun insertMenuItem(
        @Body item: MenuItem
    ): Response<List<MenuItem>>

    @PATCH("menu_items")
    suspend fun updateMenuItem(
        @Query("id") idQuery: String,
        @Body item: MenuItem
    ): Response<List<MenuItem>>

    @DELETE("menu_items")
    suspend fun deleteMenuItem(
        @Query("id") idQuery: String
    ): Response<Unit>

    // --- TOPIK 2: VOUCHERS (CRUD) ---
    @GET("vouchers")
    suspend fun getVouchers(
        @Query("select") select: String = "*",
        @Query("order") order: String = "id.asc"
    ): Response<List<Voucher>>

    @POST("vouchers")
    suspend fun insertVoucher(
        @Body voucher: Voucher
    ): Response<List<Voucher>>

    @PATCH("vouchers")
    suspend fun updateVoucher(
        @Query("id") idQuery: String,
        @Body voucher: Voucher
    ): Response<List<Voucher>>

    @DELETE("vouchers")
    suspend fun deleteVoucher(
        @Query("id") idQuery: String
    ): Response<Unit>
}
