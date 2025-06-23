package com.keelim.common.di

import com.keelim.common.KeelimDispatchers.DEFAULT
import com.keelim.common.KeelimDispatchers.IO
import com.keelim.common.KeelimDispatchers.MAIN
import com.keelim.common.KeelimDispatchers.MAIN_IMMEDIATE
import com.keelim.common.KeelimDispatchers.UNCONFINED
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @com.keelim.common.Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @com.keelim.common.Dispatcher(MAIN)
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @com.keelim.common.Dispatcher(MAIN_IMMEDIATE)
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

    @Provides
    @com.keelim.common.Dispatcher(DEFAULT)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @com.keelim.common.Dispatcher(UNCONFINED)
    fun providesUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    @Provides
    @Singleton
    @ApplicationScope
    fun providesCoroutineScope(
        @com.keelim.common.Dispatcher(IO) dispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationScope
