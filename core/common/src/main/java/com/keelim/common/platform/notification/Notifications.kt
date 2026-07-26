package com.keelim.common.platform.notification

import java.time.Instant

data class NotificationChannelKey(
    val id: String,
    val name: String,
    val description: String,
)

data class NotificationContent(
    val title: String,
    val body: String,
    val publicTitle: String? = null,
    val publicBody: String? = null,
)

data class NotificationRequest(
    val id: String,
    val scheduledAt: Instant,
    val channel: NotificationChannelKey,
    val content: NotificationContent,
    val tag: String? = null,
)

sealed interface NotificationScheduleResult {
    data object Scheduled : NotificationScheduleResult
    data object PermissionDenied : NotificationScheduleResult
    data class Invalid(val reasons: List<String>) : NotificationScheduleResult
}

interface NotificationScheduler {
    suspend fun schedule(request: NotificationRequest): NotificationScheduleResult
    suspend fun cancel(id: String)
    suspend fun cancelByTag(tag: String)
}

object NotificationPolicy {
    private const val MAX_ID_LENGTH = 100
    private const val MAX_TITLE_LENGTH = 120
    private const val MAX_BODY_LENGTH = 500
    const val PUBLIC_TITLE = "알림이 도착했어요."
    const val PUBLIC_BODY = "앱을 열어 예정된 활동을 확인하세요."

    fun violations(request: NotificationRequest): List<String> = buildList {
        if (request.id.isBlank()) add("notification id must not be blank")
        if (request.id.length > MAX_ID_LENGTH) add("notification id is too long")
        if (request.channel.id.isBlank()) add("notification channel id must not be blank")
        if (request.channel.name.isBlank()) add("notification channel name must not be blank")
        if (request.content.title.isBlank()) add("notification title must not be blank")
        if (request.content.title.length > MAX_TITLE_LENGTH) add("notification title is too long")
        if (request.content.body.isBlank()) add("notification body must not be blank")
        if (request.content.body.length > MAX_BODY_LENGTH) add("notification body is too long")
    }

    fun publicContent(content: NotificationContent): NotificationContent =
        content.copy(
            publicTitle = content.publicTitle?.takeIf(String::isNotBlank) ?: PUBLIC_TITLE,
            publicBody = content.publicBody?.takeIf(String::isNotBlank) ?: PUBLIC_BODY,
        )
}
