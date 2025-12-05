package com.example.bucqetbunga.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    companion object {
        private const val PREF_NAME = "BouquetAppSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "name"
    }

    fun createLoginSession(email: String, name: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NAME, name)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(): String? {
        return pref.getString(KEY_EMAIL, null)
    }

    fun getUserName(): String? {
        return pref.getString(KEY_NAME, null)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
