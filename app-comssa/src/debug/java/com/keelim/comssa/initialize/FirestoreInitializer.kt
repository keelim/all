package com.keelim.comssa.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.initialize

class FirestoreInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        val firestore = Firebase.firestore
        firestore.useEmulator("10.0.2.2", 8080)

        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = false
        }
    }

    override fun dependencies(): List<Class<Initializer<*>>> = listOf()
}
