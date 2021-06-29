package com.keelim.comssa.data.api

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.keelim.comssa.data.model.Review
import kotlinx.coroutines.tasks.await

class ReviewApiImpl:ReviewApi {
    override suspend fun getLatestReview(dataId: String): Review? {
        val fireStore = Firebase.firestore
        return fireStore.collection("reviews")
            .whereEqualTo("dataId", dataId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .map { it.toObject<Review>() }
            .firstOrNull()
    }
}