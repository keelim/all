package com.keelim.data.di

import com.keelim.data.source.BookRepository
import com.keelim.data.source.BookRepositoryImpl
import com.keelim.data.source.DefaultTaskRepository
import com.keelim.data.source.DefaultTaskRepositoryImpl
import com.keelim.data.source.HistoryRepository
import com.keelim.data.source.HistoryRepositoryImpl
import com.keelim.data.source.NandaIORepository
import com.keelim.data.source.NandaIORepositoryImpl
import com.keelim.data.source.firebase.FirebaseRepository
import com.keelim.data.source.firebase.FirebaseRepositoryImpl
import com.keelim.data.source.note.NoteRepository
import com.keelim.data.source.note.NoteRepositoryImpl
import com.keelim.data.source.notification.NotificationRepository
import com.keelim.data.source.notification.NotificationRepositoryImpl
import com.keelim.data.source.prompt.PromptRepository
import com.keelim.data.source.prompt.PromptRepositoryImpl
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
    fun bindsBookRepository(
        repository: BookRepositoryImpl,
    ): BookRepository

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
}
