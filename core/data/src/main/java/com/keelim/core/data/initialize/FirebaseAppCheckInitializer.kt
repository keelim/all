package com.keelim.core.data.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.keelim.core.data.BuildConfig

class FirebaseAppCheckInitializer : Initializer<FirebaseAppCheck> {
    override fun create(context: Context): FirebaseAppCheck {
        return Firebase.appCheck.apply {
            val provider = if (BuildConfig.DEBUG) {
                DebugAppCheckProviderFactory.getInstance()
            } else {
                PlayIntegrityAppCheckProviderFactory.getInstance()
            }
            installAppCheckProviderFactory(provider)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> =
        listOf(FirebaseInitializer::class.java)
}
