package com.keelim.core.data.di

import com.keelim.core.data.source.ArduconRepositoryImpl
import com.keelim.core.data.source.DefaultTaskRepositoryImpl
import com.keelim.core.data.source.HistoryRepositoryImpl
import com.keelim.core.data.source.TimeRepositoryImpl
import com.keelim.core.data.source.alarm.AlarmRepositoryImpl
import com.keelim.core.data.source.calculator.CalculatorHistoryRepositoryImpl
import com.keelim.core.data.source.finance.FinanceRssRepositoryImpl
import com.keelim.core.data.source.firebase.FirebaseRepositoryImpl
import com.keelim.core.data.source.length.LengthRepositoryImpl
import com.keelim.core.data.source.linkinspector.LinkInspectorRepositoryImpl
import com.keelim.core.data.source.note.NoteRepositoryImpl
import com.keelim.core.data.source.notification.NotificationRepositoryImpl
import com.keelim.core.data.source.wellness.WellnessRepositoryImpl
import com.keelim.core.data.source.prompt.PromptRepositoryImpl
import com.keelim.core.data.source.analytics.StudyAnalyticsRepositoryImpl
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
import com.keelim.data.repository.WellnessRepository
import com.keelim.data.repository.StudyAnalyticsRepository
import com.keelim.data.repository.calculator.CalculatorHistoryRepository
import com.keelim.data.repository.linkinspector.LinkInspectorRepository
import com.keelim.core.data.source.NandaRepositoryImpl
import com.keelim.data.repository.NandaRepository
import com.keelim.core.data.source.StationRepositoryImpl
import com.keelim.data.repository.StationRepository
import com.keelim.core.data.repository.Base64Repository
import com.keelim.core.data.source.Base64RepositoryImpl
import com.keelim.core.data.repository.ShortenedUrlRepository
import com.keelim.core.data.source.ShortenedUrlRepositoryImpl
import com.keelim.core.data.repository.MarketNotificationRepositoryImpl
import com.keelim.data.repository.MarketNotificationRepository
import com.keelim.core.data.repository.MedicationRepositoryImpl
import com.keelim.data.repository.MedicationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindsWellnessRepository(repository: WellnessRepositoryImpl): WellnessRepository

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

    @Binds
    fun bindsLinkInspectorRepository(
        repository: LinkInspectorRepositoryImpl,
    ): LinkInspectorRepository

    @Binds
    fun bindsCalculatorHistoryRepository(
        repository: CalculatorHistoryRepositoryImpl,
    ): CalculatorHistoryRepository

    @Binds
    fun bindsNandaRepository(
        repository: NandaRepositoryImpl,
    ): NandaRepository

    @Binds
    fun bindsStationRepository(
        repository: StationRepositoryImpl,
    ): StationRepository

    @Binds
    abstract fun bindsBase64Repository(
        repository: Base64RepositoryImpl,
    ): Base64Repository

    @Binds
    abstract fun bindsShortenedUrlRepository(
        repository: ShortenedUrlRepositoryImpl,
    ): ShortenedUrlRepository

    @Binds
    fun bindsStudyAnalyticsRepository(
        repository: StudyAnalyticsRepositoryImpl,
    ): StudyAnalyticsRepository

    @Binds
    fun bindsMarketNotificationRepository(
        repository: MarketNotificationRepositoryImpl,
    ): MarketNotificationRepository

    @Binds
    fun bindsMedicationRepository(
        repository: MedicationRepositoryImpl,
    ): MedicationRepository
}
