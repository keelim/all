package com.keelim.core.data.di

import com.keelim.core.database.repository.ArduconRepository
import com.keelim.core.data.source.ArduconRepositoryImpl
import com.keelim.core.database.repository.DefaultTaskRepository
import com.keelim.core.data.source.DefaultTaskRepositoryImpl
import com.keelim.core.database.repository.HistoryRepository
import com.keelim.core.data.source.HistoryRepositoryImpl
import com.keelim.core.database.repository.NandaIORepository
import com.keelim.core.data.source.NandaIORepositoryImpl
import com.keelim.data.repository.FirebaseRepository
import com.keelim.core.data.source.firebase.FirebaseRepositoryImpl
import com.keelim.data.repository.NoteRepository
import com.keelim.core.data.source.note.NoteRepositoryImpl
import com.keelim.data.repository.NotificationRepository
import com.keelim.core.data.source.notification.NotificationRepositoryImpl
import com.keelim.data.repository.PromptRepository
import com.keelim.core.data.source.prompt.PromptRepositoryImpl
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
    fun bindsNandaIoRepository(
        repository: NandaIORepositoryImpl,
    ): NandaIORepository

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
}
