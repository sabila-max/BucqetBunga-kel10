package com.example.bucqetbunga.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    // SharedPreferences = penyimpanan key-value lokal Android
    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = pref.edit()

    companion object {
        // Constant names (biar gak typo)
        private const val PREF_NAME = "BouquetAppSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "name"
    }

    // Simpan data login
    fun createLoginSession(email: String, name: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NAME, name)
        editor.apply()  // Commit perubahan
    }

    // Cek apakah user sudah login
    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
        // Return false kalau key tidak ada
    }

    fun getUserEmail(): String? {
        return pref.getString(KEY_EMAIL, null)
    }

    fun getUserName(): String? {
        return pref.getString(KEY_NAME, null)
    }

    // Logout = hapus semua data session
    fun logout() {
        editor.clear()
        editor.apply()
    }
}
