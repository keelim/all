package com.keelim.comssa.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Review(
    @DocumentId
    val id: String? = null,

    @ServerTimestamp
    val createdAt: Date? = null,

    val userId: String? = null,
    val dataId: String? = null,
    val content: String? = null,
    val score: Float? = null
)