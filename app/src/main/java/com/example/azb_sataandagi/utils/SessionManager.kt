package com.example.azb_sataandagi.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "SobremesaSession"
        private const val KEY_IS_SPLASH_SHOWN = "is_splash_shown"
        private const val KEY_IS_LOGIN = "is_login"
        private const val KEY_USERNAME = "username"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_PHONE = "phone"
        private const val KEY_REGISTERED_USERNAME = "reg_username"
        private const val KEY_REGISTERED_PASSWORD = "reg_password"
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

    var fullName: String
        get() = prefs.getString(KEY_FULL_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_FULL_NAME, value).apply()

    var phone: String
        get() = prefs.getString(KEY_PHONE, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PHONE, value).apply()

    var registeredUsername: String
        get() = prefs.getString(KEY_REGISTERED_USERNAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_REGISTERED_USERNAME, value).apply()

    var registeredPassword: String
        get() = prefs.getString(KEY_REGISTERED_PASSWORD, "") ?: ""
        set(value) = prefs.edit().putString(KEY_REGISTERED_PASSWORD, value).apply()

    fun registerUser(name: String, phoneNum: String, user: String, pass: String) {
        prefs.edit()
            .putString(KEY_FULL_NAME, name)
            .putString(KEY_PHONE, phoneNum)
            .putString(KEY_REGISTERED_USERNAME, user)
            .putString(KEY_REGISTERED_PASSWORD, pass)
            .apply()
    }

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGIN, false)
            .remove(KEY_USERNAME)
            .apply()
    }
}
