package com.keelim.comssa.data.repository

import com.keelim.comssa.data.model.Review

interface ReviewRepository {
    suspend fun getLatestReview(dataId:String): Review?
}