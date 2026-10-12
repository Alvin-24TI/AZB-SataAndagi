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

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGIN, false)
            .remove(KEY_USERNAME)
            .apply()
    }
}
