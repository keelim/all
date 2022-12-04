/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
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
package com.keelim.comssa.di

import com.keelim.comssa.data.api.DataApi
import com.keelim.comssa.data.api.DataApiImpl
import com.keelim.comssa.data.api.ReviewApi
import com.keelim.comssa.data.api.ReviewApiImpl
import com.keelim.comssa.data.api.UserApi
import com.keelim.comssa.data.api.UserApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    abstract fun bindsDataApi(dataApiImpl: DataApiImpl): DataApi

    @Binds
    abstract fun bindsReviewApi(reviewApiImpl: ReviewApiImpl): ReviewApi

    @Binds
    abstract fun bindsUserApi(userApiImpl: UserApiImpl): UserApi
}
