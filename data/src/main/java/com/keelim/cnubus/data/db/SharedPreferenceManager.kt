package com.keelim.cnubus.data.db

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class SharedPreferenceManager(
    private val sharedPreferences: SharedPreferences
) {

    fun getLong(key: String): Long? {
        val value = sharedPreferences.getLong(key, INVALID_LONG_VALUE)

        return if (value == INVALID_LONG_VALUE) {
            null
        } else {
            value
        }
    }

    fun putLong(key: String, value: Long) =
        sharedPreferences.edit { putLong(key, value) }

    companion object {
        private const val INVALID_LONG_VALUE = Long.MIN_VALUE
    }
}
