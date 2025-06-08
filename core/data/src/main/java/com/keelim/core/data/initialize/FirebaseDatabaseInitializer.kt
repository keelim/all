package com.keelim.core.data.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.database
import com.keelim.core.data.BuildConfig

class FirebaseDatabaseInitializer : Initializer<FirebaseDatabase> {
    override fun create(context: Context): FirebaseDatabase {
        return Firebase.database.apply {
            if (BuildConfig.DEBUG) {
                setLogLevel(Logger.Level.DEBUG)
            }
            setPersistenceEnabled(true)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = listOf(
        FirebaseInitializer::class.java
    )
}
