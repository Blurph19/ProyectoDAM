package com.example.tripplanner.utils

import android.content.Context

object SessionManager {

    private const val PREF_NAME = "travelplanify_session"
    private const val KEY_USER_ID = "user_id"

    fun saveUserId(context: Context, userId: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}