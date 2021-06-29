package com.keelim.comssa.data.repository

import com.keelim.comssa.data.api.ReviewApi
import com.keelim.comssa.data.model.Review
import com.keelim.comssa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ReviewRepository {
    override suspend fun getLatestReview(dataId: String): Review? = withContext(ioDispatcher) {
        return@withContext reviewApi.getLatestReview(dataId = dataId)
    }
}