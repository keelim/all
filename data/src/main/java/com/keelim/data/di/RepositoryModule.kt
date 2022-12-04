package com.keelim.data.di

import com.keelim.data.repository.IoRepository
import com.keelim.data.repository.IoRepositoryImpl
import com.keelim.data.repository.io.IORepository
import com.keelim.data.repository.io.IORepositoryImpl
import com.keelim.data.repository.setting.DeveloperRepository
import com.keelim.data.repository.setting.DeveloperRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindsIoRepository(
        repository: IoRepositoryImpl,
    ): IoRepository

    @Binds
    abstract fun bindsDeveloperRepository(
        repository: DeveloperRepositoryImpl,
    ): DeveloperRepository

    @Binds
    abstract fun bindsIoRepository(
        repository: IORepositoryImpl,
    ): IORepository
}
