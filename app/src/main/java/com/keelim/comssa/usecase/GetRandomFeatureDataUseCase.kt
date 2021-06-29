package com.keelim.comssa.usecase

import com.keelim.comssa.data.model.FeaturedData
import com.keelim.comssa.data.repository.DataRepository
import com.keelim.comssa.data.repository.ReviewRepository
import javax.inject.Inject

class GetRandomFeatureDataUseCase @Inject constructor(
    private val dataRepository:DataRepository,
    private val reviewRepository: ReviewRepository,
){
    suspend operator fun invoke(): FeaturedData?{
        val featuredDatas = dataRepository.getAllDatas()
            .filter { it.id.isNullOrBlank().not() }
            .filter { it.isFeatured == true }

        if (featuredDatas.isNullOrEmpty()) {
            return null
        }

        return featuredDatas.random()
            .let { data ->
                val latestReview = reviewRepository.getLatestReview(data.id!!)
                FeaturedData(data, latestReview)
            }
    }
}