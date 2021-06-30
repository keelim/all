package com.keelim.comssa.data.preference

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(
    @ApplicationContext context: Context,
) : PreferenceManager {
    private val sharedPreferences = context.getSharedPreferences("preference", Activity.MODE_PRIVATE)
    override fun getString(key: String): String? =
        sharedPreferences.getString(key, null)

    override fun putString(key: String, value: String){
        sharedPreferences.edit { putString(key, value) }
    }
}