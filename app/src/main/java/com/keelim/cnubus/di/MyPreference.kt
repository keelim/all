package com.keelim.cnubus.di

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(
    @ApplicationContext context : Context
){
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getFirstOpen(): Boolean {
        return prefs.getBoolean("FIRST_OPEN", false)
    }

    fun setFirstOpen(flag:Boolean = true){
        prefs.edit().putBoolean("FIRST_OPEN", flag).apply()
    }
}