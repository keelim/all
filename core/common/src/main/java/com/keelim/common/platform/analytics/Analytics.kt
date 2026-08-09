package com.keelim.common.platform.analytics

import jakarta.inject.Inject
import java.util.Locale

data class AnalyticsEvent(
    val name: String,
    val properties: Map<String, String> = emptyMap(),
)

enum class AnalyticsConsentState {
    DISABLED,
    ENABLED,
}

fun interface AnalyticsTracker {
    fun track(event: AnalyticsEvent)
}

object AnalyticsEventPolicy {
    const val MAX_NAME_LENGTH = 40
    const val MAX_PROPERTIES = 20
    const val MAX_PROPERTY_VALUE_LENGTH = 100

    private val eventName = Regex("[a-z][a-z0-9_]*")
    private val forbiddenKeys = setOf(
        "libido_score",
        "erection_status",
        "pain_status",
        "health_answer",
        "measurement_length",
        "circumference",
        "supplement_name",
        "medication_name",
        "free_text_note",
        "diagnosis",
        "exact_birth_date",
    )

    fun violations(event: AnalyticsEvent): List<String> = buildList {
        if (event.name.length !in 1..MAX_NAME_LENGTH || !eventName.matches(event.name)) {
            add("event name must be snake_case and at most $MAX_NAME_LENGTH characters")
        }
        if (event.properties.size > MAX_PROPERTIES) {
            add("event properties must not exceed $MAX_PROPERTIES")
        }
        event.properties.forEach { (key, value) ->
            if (key.lowercase(Locale.ROOT) in forbiddenKeys) {
                add("forbidden analytics property: $key")
            }
            if (value.length > MAX_PROPERTY_VALUE_LENGTH) {
                add("analytics property value is too long: $key")
            }
        }
    }
}

class NoOpAnalyticsTracker @Inject constructor() : AnalyticsTracker {
    override fun track(event: AnalyticsEvent) = Unit
}

class DebugAnalyticsTracker(
    private val sink: (AnalyticsEvent) -> Unit,
) : AnalyticsTracker {
    override fun track(event: AnalyticsEvent) {
        val violations = AnalyticsEventPolicy.violations(event)
        require(violations.isEmpty()) { violations.joinToString() }
        sink(event)
    }
}

class ConsentAwareAnalyticsTracker(
    private val delegate: AnalyticsTracker,
    private val consent: () -> AnalyticsConsentState,
) : AnalyticsTracker {
    override fun track(event: AnalyticsEvent) {
        if (consent() == AnalyticsConsentState.ENABLED) delegate.track(event)
    }
}
