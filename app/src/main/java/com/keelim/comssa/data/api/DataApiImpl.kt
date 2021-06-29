package com.keelim.comssa.data.api

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.keelim.comssa.data.model.Data
import kotlinx.coroutines.tasks.await

class DataApiImpl() : DataApi {
    override suspend fun getAllData(): List<Data> {
        val fireStore = Firebase.firestore
        return fireStore.collection("datas")
            .get()
            .await()
            .map { it.toObject<Data>() }
    }
}