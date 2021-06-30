package com.keelim.comssa.data.api

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.keelim.comssa.data.model.Data
import com.keelim.comssa.data.model.Review
import kotlinx.coroutines.tasks.await

class ReviewApiImpl:ReviewApi {
    private val fireStore = Firebase.firestore

    override suspend fun getLatestReview(dataId: String): Review? {
        return fireStore.collection("reviews")
            .whereEqualTo("dataId", dataId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .map { it.toObject<Review>() }
            .firstOrNull()
    }


    override suspend fun getAllReviews(dataId: String): List<Review>{
        return fireStore.collection("reviews")
            .whereEqualTo("dataId", dataId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .map { it.toObject<Review>() }
    }

    override suspend fun getAllUserReviews(userIds: String): List<Review> {
        return fireStore.collection("reviews")
            .whereEqualTo("userId", userIds)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .map { it.toObject<Review>() }
    }

    override suspend fun addReview(review: Review): Review {
        val newReviewReference = fireStore.collection("reviews").document()
        val dataReference = fireStore.collection("datas").document(review.dataId!!)

        fireStore.runTransaction { transaction ->
            val data = transaction.get(dataReference).toObject<Data>()!!

            val oldAverageScore = data.averageScore ?: 0f
            val oldNumberOfScore = data.numberOfScore ?: 0
            val oldTotalScore = oldAverageScore * oldNumberOfScore

            val newNumberOfScore = oldNumberOfScore + 1
            val newAverageScore = (oldTotalScore + (review.score ?: 0f)) / newNumberOfScore

            transaction.set(
                dataReference,
                data.copy(
                    numberOfScore = newNumberOfScore,
                    averageScore = newAverageScore
                )
            )

            transaction.set(
                newReviewReference,
                review,
                SetOptions.merge()
            )
        }.await()

        return newReviewReference.get().await().toObject<Review>()!!
    }

    override suspend fun removeReview(review: Review) {
        val reviewReference = fireStore.collection("reviews").document(review.id!!)
        val dataReference = fireStore.collection("datas").document(review.dataId!!)

        fireStore.runTransaction { transaction ->
            val data = transaction
                .get(dataReference)
                .toObject<Data>()!!

            val oldAverageScore = data.averageScore ?: 0f
            val oldNumberOfScore = data.numberOfScore ?: 0
            val oldTotalScore = oldAverageScore * oldNumberOfScore

            val newNumberOfScore = (oldNumberOfScore - 1).coerceAtLeast(0)
            val newAverageScore = if (newNumberOfScore > 0) {
                (oldTotalScore - (review.score ?: 0f)) / newNumberOfScore
            } else {
                0f
            }

            transaction.set(
                dataReference,
                data.copy(
                    numberOfScore = newNumberOfScore,
                    averageScore = newAverageScore
                )
            )

            transaction.delete(reviewReference)
        }.await()
    }
}