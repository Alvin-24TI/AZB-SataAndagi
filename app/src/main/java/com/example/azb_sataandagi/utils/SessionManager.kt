package com.example.azb_sataandagi.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.azb_sataandagi.model.Member
import org.json.JSONArray
import org.json.JSONObject

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "SobremesaSession"
        private const val KEY_IS_SPLASH_SHOWN = "is_splash_shown"
        private const val KEY_IS_LOGIN = "is_login"
        private const val KEY_USERNAME = "username"
        private const val KEY_REGISTERED_USERNAME = "registered_username"
        private const val KEY_REGISTERED_PASSWORD = "registered_password"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_PHONE = "phone"
        private const val KEY_IS_MEMBER_REGISTERED = "is_member_registered"
        private const val KEY_MEMBER_TIER = "member_tier"
        private const val KEY_MEMBER_POINTS = "member_points"
        private const val KEY_CLAIMED_VOUCHERS = "claimed_vouchers"
        private const val KEY_MEMBER_LIST_JSON = "member_list_json"
    }

    var isSplashShown: Boolean
        get() = prefs.getBoolean(KEY_IS_SPLASH_SHOWN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_SPLASH_SHOWN, value).apply()

    var isLogin: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGIN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGIN, value).apply()

    var username: String
        get() = prefs.getString(KEY_USERNAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USERNAME, value).apply()

    var registeredUsername: String
        get() = prefs.getString(KEY_REGISTERED_USERNAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_REGISTERED_USERNAME, value).apply()

    var registeredPassword: String
        get() = prefs.getString(KEY_REGISTERED_PASSWORD, "") ?: ""
        set(value) = prefs.edit().putString(KEY_REGISTERED_PASSWORD, value).apply()

    var fullName: String
        get() = prefs.getString(KEY_FULL_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_FULL_NAME, value).apply()

    var phone: String
        get() = prefs.getString(KEY_PHONE, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PHONE, value).apply()

    fun registerUser(nama: String, phoneNum: String, user: String, pass: String) {
        fullName = nama
        phone = phoneNum
        registeredUsername = user
        registeredPassword = pass
        username = user
    }

    var isMemberRegistered: Boolean
        get() = prefs.getBoolean(KEY_IS_MEMBER_REGISTERED, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_MEMBER_REGISTERED, value).apply()

    var memberTier: String
        get() = prefs.getString(KEY_MEMBER_TIER, "Gold VIP Member") ?: "Gold VIP Member"
        set(value) = prefs.edit().putString(KEY_MEMBER_TIER, value).apply()

    var memberPoints: Int
        get() = prefs.getInt(KEY_MEMBER_POINTS, 500)
        set(value) = prefs.edit().putInt(KEY_MEMBER_POINTS, value).apply()

    fun getClaimedVoucherIds(): Set<String> {
        return prefs.getStringSet(KEY_CLAIMED_VOUCHERS, emptySet()) ?: emptySet()
    }

    fun claimVoucher(voucherId: String) {
        val currentSet = getClaimedVoucherIds().toMutableSet()
        currentSet.add(voucherId)
        prefs.edit().putStringSet(KEY_CLAIMED_VOUCHERS, currentSet).apply()
    }

    fun getMemberList(): List<Member> {
        val jsonString = prefs.getString(KEY_MEMBER_LIST_JSON, null)
        if (jsonString.isNullOrEmpty()) {
            return listOf(
                Member("Alvin Z. B.", "081234567890", "alvin@sobremesa.com", "Gold VIP", "10-10-2025"),
                Member("Budi Santoso", "085712345678", "budi.santoso@gmail.com", "Silver Member", "12-10-2025"),
                Member("Siti Rahmawati", "081987654321", "siti.rahma@gmail.com", "Platinum Member", "15-10-2025")
            )
        }
        val list = mutableListOf<Member>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Member(
                        fullName = obj.getString("fullName"),
                        phone = obj.getString("phone"),
                        email = obj.getString("email"),
                        tier = obj.getString("tier"),
                        registerDate = obj.optString("registerDate", "Baru Saja")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun addMember(member: Member) {
        val currentList = getMemberList().toMutableList()
        currentList.add(0, member)
        val jsonArray = JSONArray()
        for (m in currentList) {
            val obj = JSONObject()
            obj.put("fullName", m.fullName)
            obj.put("phone", m.phone)
            obj.put("email", m.email)
            obj.put("tier", m.tier)
            obj.put("registerDate", m.registerDate)
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_MEMBER_LIST_JSON, jsonArray.toString()).apply()
    }

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGIN, false)
            .remove(KEY_USERNAME)
            .remove(KEY_IS_MEMBER_REGISTERED)
            .remove(KEY_MEMBER_TIER)
            .apply()
    }
}
