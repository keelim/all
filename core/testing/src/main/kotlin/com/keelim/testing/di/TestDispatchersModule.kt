package com.keelim.testing.di

import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers.DEFAULT
import com.keelim.common.KeelimDispatchers.IO
import com.keelim.common.di.DispatchersModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class],
)
object TestDispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher

    @Provides
    @Dispatcher(DEFAULT)
    fun providesDefaultDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher
}
