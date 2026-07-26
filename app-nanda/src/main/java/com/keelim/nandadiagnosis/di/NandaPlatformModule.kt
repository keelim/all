package com.keelim.nandadiagnosis.di

import android.content.Context
import com.keelim.common.platform.analytics.AnalyticsTracker
import com.keelim.common.platform.analytics.NoOpAnalyticsTracker
import com.keelim.common.platform.export.ExportFileWriter
import com.keelim.common.platform.export.ExportShareLauncher
import com.keelim.common.platform.featureflag.DefaultFeatureFlagProvider
import com.keelim.common.platform.featureflag.FeatureFlagProvider
import com.keelim.common.platform.notification.NotificationScheduler
import com.keelim.common.platform.observability.AppLogger
import com.keelim.common.platform.observability.CrashReporter
import com.keelim.common.platform.observability.NoOpAppLogger
import com.keelim.common.platform.observability.NoOpCrashReporter
import com.keelim.common.platform.privacy.DefaultPrivacyController
import com.keelim.common.platform.privacy.PrivacyController
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.common.platform.privacy.PrivacySettingsRepository
import com.keelim.common.platform.time.SystemTimeProvider
import com.keelim.common.platform.time.TimeProvider
import com.keelim.commonAndroid.platform.export.AndroidExportFileWriter
import com.keelim.commonAndroid.platform.export.AndroidExportShareLauncher
import com.keelim.commonAndroid.platform.notification.WorkManagerNotificationScheduler
import com.keelim.commonAndroid.platform.privacy.DataStorePrivacySettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import java.time.Duration

@Module
@InstallIn(SingletonComponent::class)
object NandaPlatformModule {
    @Provides
    @Singleton
    fun provideTimeProvider(): TimeProvider = SystemTimeProvider()

    @Provides
    @Singleton
    fun provideAnalyticsTracker(): AnalyticsTracker = NoOpAnalyticsTracker()

    @Provides
    @Singleton
    fun provideFeatureFlagProvider(): FeatureFlagProvider = DefaultFeatureFlagProvider()

    @Provides
    @Singleton
    fun provideAppLogger(): AppLogger = NoOpAppLogger()

    @Provides
    @Singleton
    fun provideCrashReporter(): CrashReporter = NoOpCrashReporter()

    @Provides
    @Singleton
    fun providePrivacySettingsRepository(
        @ApplicationContext context: Context,
    ): PrivacySettingsRepository = DataStorePrivacySettingsRepository(
        context = context,
        defaults = PrivacySettings(
            appLockEnabled = false,
            obscureRecentApps = true,
            blockScreenshots = true,
            autoLockTimeout = Duration.ofMinutes(5),
        ),
    )

    @Provides
    @Singleton
    fun providePrivacyController(timeProvider: TimeProvider): PrivacyController =
        DefaultPrivacyController(timeProvider)

    @Provides
    @Singleton
    fun provideNotificationScheduler(
        @ApplicationContext context: Context,
    ): NotificationScheduler = WorkManagerNotificationScheduler(context)

    @Provides
    @Singleton
    fun provideExportFileWriter(
        @ApplicationContext context: Context,
    ): ExportFileWriter = AndroidExportFileWriter(context)

    @Provides
    @Singleton
    fun provideExportShareLauncher(
        @ApplicationContext context: Context,
    ): ExportShareLauncher = AndroidExportShareLauncher(context)
}
