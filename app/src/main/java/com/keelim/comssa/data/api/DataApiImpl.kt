package com.keelim.comssa.data.api

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.keelim.comssa.data.model.Data
import kotlinx.coroutines.tasks.await

class DataApiImpl() : DataApi {
    private val fireStore = Firebase.firestore

    override suspend fun getAllData(): List<Data> {
        return fireStore.collection("datas")
            .get()
            .await()
            .map { it.toObject<Data>() }
    }

    override suspend fun getDatas(dataIds: List<String>): List<Data> {
        return fireStore.collection("datas")
            .whereIn(FieldPath.documentId(), dataIds)
            .get()
            .await()
            .map { it.toObject<Data>() }
    }
}