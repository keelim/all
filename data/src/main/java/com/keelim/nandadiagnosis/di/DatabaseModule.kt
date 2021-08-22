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

import android.content.Context
import androidx.room.Room
import com.keelim.nandadiagnosis.data.db.AppDatabaseV2
import com.keelim.nandadiagnosis.data.network.NandaService
import com.keelim.nandadiagnosis.data.repository.RemoteDataSource
import com.keelim.nandadiagnosis.data.repository.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideAppDatabase(
    @ApplicationContext context: Context,
  ): AppDatabaseV2 {
    return Room.databaseBuilder(
      context.applicationContext,
      AppDatabaseV2::class.java,
      "nanda"
    )
      .createFromFile(File(context.getExternalFilesDir(null), "nanda.db"))
      .allowMainThreadQueries()
      .build()
  }

  @Provides
  @Singleton
  fun provideRemoteDataSource(nandaService: NandaService): RemoteDataSource {
    return RemoteDataSourceImpl(nandaService)
  }
}
