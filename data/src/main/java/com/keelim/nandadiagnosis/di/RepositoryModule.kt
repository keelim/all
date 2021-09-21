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
package com.keelim.nandadiagnosis.di

import com.keelim.nandadiagnosis.data.repository.IORepository
import com.keelim.nandadiagnosis.data.repository.IORepositoryImpl
import com.keelim.nandadiagnosis.data.repository.setting.DeveloperRepository
import com.keelim.nandadiagnosis.data.repository.setting.DeveloperRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher


@InstallIn(SingletonComponent::class)
@Module(includes = [RepositoryModule.IoModule::class])
internal abstract class RepositoryModule {

  @Binds
  abstract fun bindsDeveloperRepository(
    repository: DeveloperRepositoryImpl,
  ): DeveloperRepository

  @InstallIn(SingletonComponent::class)
  @Module
  internal object IoModule {
    @Provides
    @Singleton
    fun providerIORepository(
      nandaService: com.keelim.nandadiagnosis.data.network.NandaService,
      @IoDispatcher ioDispatcher: CoroutineDispatcher,
      @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
      db: com.keelim.nandadiagnosis.data.db.AppDatabaseV2,
    ): IORepository {
      return IORepositoryImpl(
        nandaService = nandaService,
        ioDispatcher = ioDispatcher,
        defaultDispatcher = defaultDispatcher,
        db = db,
      )
    }
  }
}
