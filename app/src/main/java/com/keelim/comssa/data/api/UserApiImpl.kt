package com.keelim.comssa.data.api

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.keelim.comssa.data.model.User
import kotlinx.coroutines.tasks.await

class UserApiImpl: UserApi {
    private val fireStore = Firebase.firestore
    override suspend fun saveUser(user: User): User {
        return fireStore.collection("users")
            .add(user)
            .await()
            .let { User(it.id) }
    }
}