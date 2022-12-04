package com.keelim.data.di

import com.keelim.data.repository.IoRepository
import com.keelim.data.repository.IoRepositoryImpl
import com.keelim.data.repository.NandaIORepository
import com.keelim.data.repository.NandaIORepositoryImpl
import com.keelim.data.repository.setting.DeveloperRepository
import com.keelim.data.repository.setting.DeveloperRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsIoRepository(
        repository: IoRepositoryImpl,
    ): IoRepository

    @Binds
    abstract fun bindsDeveloperRepository(
        repository: DeveloperRepositoryImpl,
    ): DeveloperRepository

    @Binds
    abstract fun bindsNandaIoRepository(
        repository: NandaIORepositoryImpl,
    ): NandaIORepository
}
