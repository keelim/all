package com.keelim.core.data.di

import com.keelim.core.data.source.ArduconRepositoryImpl
import com.keelim.core.data.source.DefaultTaskRepositoryImpl
import com.keelim.core.data.source.HistoryRepositoryImpl
import com.keelim.core.data.source.TimeRepositoryImpl
import com.keelim.core.data.source.alarm.AlarmRepositoryImpl
import com.keelim.core.data.source.firebase.FirebaseRepositoryImpl
import com.keelim.core.data.source.finance.FinanceRssRepositoryImpl
import com.keelim.core.data.source.length.LengthRepositoryImpl
import com.keelim.core.data.source.note.NoteRepositoryImpl
import com.keelim.core.data.source.notification.NotificationRepositoryImpl
import com.keelim.core.data.source.prompt.PromptRepositoryImpl
import com.keelim.data.repository.AlarmRepository
import com.keelim.data.repository.ArduconRepository
import com.keelim.data.repository.DefaultTaskRepository
import com.keelim.data.repository.FinanceRssRepository
import com.keelim.data.repository.FirebaseRepository
import com.keelim.data.repository.HistoryRepository
import com.keelim.data.repository.LengthRepository
import com.keelim.data.repository.NoteRepository
import com.keelim.data.repository.NotificationRepository
import com.keelim.data.repository.PromptRepository
import com.keelim.data.repository.TimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindsIoRepository(
        repository: HistoryRepositoryImpl,
    ): HistoryRepository

    @Binds
    fun bindsDefaultTaskRepository(
        repository: DefaultTaskRepositoryImpl,
    ): DefaultTaskRepository

    @Binds
    fun bindsNotificationRepository(
        repository: NotificationRepositoryImpl,
    ): NotificationRepository

    @Binds
    fun bindsPromptRepository(
        repository: PromptRepositoryImpl,
    ): PromptRepository

    @Binds
    fun bindsFirebaseRepository(
        repository: FirebaseRepositoryImpl,
    ): FirebaseRepository

    @Binds
    fun bindsNoteRepository(
        repository: NoteRepositoryImpl,
    ): NoteRepository

    @Binds
    fun bindsArduconRepository(
        repository: ArduconRepositoryImpl,
    ): ArduconRepository

    @Binds
    fun bindsAlarmRepository(
        repository: AlarmRepositoryImpl,
    ): AlarmRepository

    @Binds
    fun bindsTimeRepository(
        repository: TimeRepositoryImpl,
    ): TimeRepository

    @Binds
    fun bindsLengthRepository(
        repository: LengthRepositoryImpl,
    ): LengthRepository

    @Binds
    fun bindsFinanceRssRepository(
        repository: FinanceRssRepositoryImpl,
    ): FinanceRssRepository
}
