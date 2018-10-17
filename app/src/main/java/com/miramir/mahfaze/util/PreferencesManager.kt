package com.miramir.mahfaze.util

import android.content.Context
import androidx.preference.PreferenceManager

object PreferencesManager {
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_PASSWORD = "user_password"

    fun getUserId(context: Context): Int =
            PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_USER_ID, -1)

    fun putUserId(context: Context, id: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(KEY_USER_ID, id)
                .apply()
    }

    fun getUserEmail(context: Context): String? =
            PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_USER_EMAIL, null)

    fun putUserEmail(context: Context, email: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_USER_EMAIL, email)
                .apply()
    }

    fun getUserPassword(context: Context): String? =
            PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_USER_PASSWORD, null)

    fun putUserPassword(context: Context, password: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_USER_PASSWORD, password)
                .apply()
    }
}