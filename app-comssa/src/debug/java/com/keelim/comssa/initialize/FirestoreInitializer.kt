package com.keelim.comssa.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings

internal const val isEmulatorEnable = false
class FirestoreInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (isEmulatorEnable) {
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            val firestore = Firebase.firestore
            firestore.useEmulator("10.0.2.2", 8080)

            firestore.firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            val database = Firebase.database
            database.useEmulator("10.0.2.2", 9000)
        }
    }

    override fun dependencies(): List<Class<Initializer<*>>> = listOf()
}
