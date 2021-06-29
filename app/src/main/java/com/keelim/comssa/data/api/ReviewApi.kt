package com.keelim.comssa.data.api

import com.keelim.comssa.data.model.Review

interface ReviewApi {
    suspend fun getLatestReview(dataId:String): Review?
}