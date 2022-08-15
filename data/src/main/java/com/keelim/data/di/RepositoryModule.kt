package com.keelim.data.di

import com.keelim.data.repository.IoRepository
import com.keelim.data.repository.IoRepositoryImpl
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
}
