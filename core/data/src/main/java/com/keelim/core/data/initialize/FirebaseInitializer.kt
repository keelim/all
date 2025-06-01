package com.keelim.core.data.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize

class FirebaseInitializer : Initializer<FirebaseApp> {

    override fun create(context: Context): FirebaseApp {
        return Firebase.initialize(context)
            ?: throw IllegalStateException("Firebase initialization failed")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
