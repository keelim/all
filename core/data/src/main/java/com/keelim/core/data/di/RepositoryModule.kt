package com.keelim.core.data.di


import com.keelim.core.data.source.BookRepository
import com.keelim.core.data.source.BookRepositoryImpl
import com.keelim.core.data.source.DefaultTaskRepository
import com.keelim.core.data.source.DefaultTaskRepositoryImpl
import com.keelim.core.data.source.HistoryRepository
import com.keelim.core.data.source.HistoryRepositoryImpl
import com.keelim.core.data.source.NandaIORepository
import com.keelim.core.data.source.NandaIORepositoryImpl
import com.keelim.core.data.source.firebase.FirebaseRepository
import com.keelim.core.data.source.firebase.FirebaseRepositoryImpl
import com.keelim.core.data.source.note.NoteRepository
import com.keelim.core.data.source.note.NoteRepositoryImpl
import com.keelim.core.data.source.notification.NotificationRepository
import com.keelim.core.data.source.notification.NotificationRepositoryImpl
import com.keelim.core.data.source.prompt.PromptRepository
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
