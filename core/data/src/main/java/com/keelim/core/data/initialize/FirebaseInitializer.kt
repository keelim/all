package com.keelim.core.data.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.database.Logger
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.initialize
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.keelim.core.data.BuildConfig

internal const val isEmulatorEnable = false

class FirebaseInitializer : Initializer<Unit> {

    private val isFirestoreEnabled = false
    private val isAppCheckEnabled = false

    override fun create(context: Context) {
        Firebase.initialize(context)?.run {
            // database
            with(Firebase.database) {
                if (BuildConfig.DEBUG) {
                    setLogLevel(Logger.Level.DEBUG)
                }
                setPersistenceEnabled(true)
            }
            // remote config
            with(Firebase.remoteConfig) {
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 600
                }
                setConfigSettingsAsync(configSettings)
                fetchAndActivate()
            }
            // fire store
            if (isFirestoreEnabled) {
                with(Firebase.firestore) {
                    if (isEmulatorEnable) {
                        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
                        // the host computer from an Android emulator.
                        useEmulator("10.0.2.2", 8080)

                        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
                        // the host computer from an Android emulator.
                        val database = Firebase.database
                        database.useEmulator("10.0.2.2", 9000)
                    }

                    firestoreSettings = firestoreSettings {
                        // Use memory cache
                        setLocalCacheSettings(memoryCacheSettings {})
                        // Use persistent disk cache (default)
                        setLocalCacheSettings(persistentCacheSettings {})
                    }
                }
            }
            // firebase app check
            if (isAppCheckEnabled) {
                with(Firebase.appCheck) {
                    val provider = if (BuildConfig.DEBUG) {
                        DebugAppCheckProviderFactory.getInstance()
                    } else {
                        PlayIntegrityAppCheckProviderFactory.getInstance()
                    }
                    installAppCheckProviderFactory(provider)
                }
            }

        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
