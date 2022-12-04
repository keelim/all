/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.domain.di

import com.keelim.data.repository.NandaIORepository
import com.keelim.data.repository.theme.ThemeRepository
import com.keelim.domain.theme.ThemeUseCase
import com.keelim.domain.setting.UserUseCase
import com.keelim.domain.comssa.DeleteReviewUseCase
import com.keelim.domain.comssa.GetAllDataReviewsUseCase
import com.keelim.domain.comssa.GetAllDatasUseCase
import com.keelim.domain.comssa.GetFavoriteUseCase
import com.keelim.domain.comssa.GetRandomFeatureDataUseCase
import com.keelim.domain.comssa.GetUserReviewedDataUseCase
import com.keelim.domain.comssa.SearchUseCase
import com.keelim.domain.comssa.SubmitReviewUseCase
import com.keelim.domain.comssa.UpdateFavoriteUseCase
import com.keelim.domain.nandadiagnosis.GetFavoriteListUseCase
import com.keelim.domain.nandadiagnosis.GetNandaListUseCase
import com.keelim.domain.nandadiagnosis.GetNandaUseCase
import com.keelim.domain.nandadiagnosis.GetSearchListUseCase
import com.keelim.domain.nandadiagnosis.HistoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetNandaUseCase(nandaIoRepository: NandaIORepository) = GetNandaUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideGetNandaListUseCase(nandaIoRepository: NandaIORepository) = GetNandaListUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideGetFavoriteListUseCase(nandaIoRepository: NandaIORepository) =
        GetFavoriteListUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideGetSearchListUseCase(nandaIoRepository: NandaIORepository) = GetSearchListUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideHistoryUseCase(nandaIoRepository: NandaIORepository) = HistoryUseCase(nandaIoRepository)

    @Provides
    @ViewModelScoped
    fun provideSearchUseCase(
        comssaIoRepository: ComssaIoRepository,
    ): SearchUseCase {
        return SearchUseCase(
            comssaIoRepository = comssaIoRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateFavorite(
        comssaIoRepository: ComssaIoRepository,
    ): UpdateFavoriteUseCase {
        return UpdateFavoriteUseCase(
            comssaIoRepository = comssaIoRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun getFavorite(
        comssaIoRepository: ComssaIoRepository,
    ): GetFavoriteUseCase {
        return GetFavoriteUseCase(
            comssaIoRepository = comssaIoRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllDatasUseCase(
        dataRepository: DataRepository,
    ): GetAllDatasUseCase {
        return GetAllDatasUseCase(
            dataRepository = dataRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetRandomFeatureDataUseCase(
        dataRepository: DataRepository,
        reviewRepository: ReviewRepository,
    ): GetRandomFeatureDataUseCase {
        return GetRandomFeatureDataUseCase(
            dataRepository = dataRepository,
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetUserReviewedDataUseCase(
        userRepository: UserRepository,
        dataRepository: DataRepository,
        reviewRepository: ReviewRepository,
    ): GetUserReviewedDataUseCase {
        return GetUserReviewedDataUseCase(
            userRepository = userRepository,
            reviewRepository = reviewRepository,
            dataRepository = dataRepository,
        )
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteReviewUseCase(
        reviewRepository: ReviewRepository,
    ): DeleteReviewUseCase {
        return DeleteReviewUseCase(
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideSubmitReviewUseCase(
        userRepository: UserRepository,
        reviewRepository: ReviewRepository,
    ): SubmitReviewUseCase {
        return SubmitReviewUseCase(
            userRepository = userRepository,
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllDataReviewsUseCase(
        userRepository: UserRepository,
        reviewRepository: ReviewRepository
    ): GetAllDataReviewsUseCase {
        return GetAllDataReviewsUseCase(
            userRepository = userRepository,
            reviewRepository = reviewRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideThemeUseCase(
        themeRepository: ThemeRepository
    ): ThemeUseCase = ThemeUseCase(themeRepository)

    @Provides
    @ViewModelScoped
    fun provideUserUseCase(
        userRepository: UserRepository
    ): UserUseCase = UserUseCase(userRepository)
}
