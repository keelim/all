package com.keelim.common.platform.observability

import jakarta.inject.Inject

interface AppLogger {
    fun debug(message: String, attributes: Map<String, String> = emptyMap())
    fun info(message: String, attributes: Map<String, String> = emptyMap())
    fun warning(message: String, throwable: Throwable? = null)
    fun error(message: String, throwable: Throwable? = null)
}

interface CrashReporter {
    fun recordException(
        throwable: Throwable,
        attributes: Map<String, String> = emptyMap(),
    )
}

object LogRedactionPolicy {
    const val REDACTED = "[REDACTED]"

    private val sensitiveTerms = setOf(
        "token",
        "password",
        "authorization",
        "email",
        "phone",
        "health",
        "measurement",
        "note",
        "medication",
        "supplement",
    )

    fun redact(attributes: Map<String, String>): Map<String, String> =
        attributes.mapValues { (key, value) ->
            if (sensitiveTerms.any { key.contains(it, ignoreCase = true) }) REDACTED else value
        }
}

class NoOpAppLogger @Inject constructor() : AppLogger {
    override fun debug(message: String, attributes: Map<String, String>) = Unit
    override fun info(message: String, attributes: Map<String, String>) = Unit
    override fun warning(message: String, throwable: Throwable?) = Unit
    override fun error(message: String, throwable: Throwable?) = Unit
}

class DebugAppLogger(
    private val sink: (String) -> Unit,
) : AppLogger {
    override fun debug(message: String, attributes: Map<String, String>) =
        write("DEBUG", message, attributes)

    override fun info(message: String, attributes: Map<String, String>) =
        write("INFO", message, attributes)

    override fun warning(message: String, throwable: Throwable?) =
        sink("WARN $message${throwable?.let { " (${it::class.simpleName})" }.orEmpty()}")

    override fun error(message: String, throwable: Throwable?) =
        sink("ERROR $message${throwable?.let { " (${it::class.simpleName})" }.orEmpty()}")

    private fun write(level: String, message: String, attributes: Map<String, String>) {
        sink("$level $message ${LogRedactionPolicy.redact(attributes)}")
    }
}

class NoOpCrashReporter @Inject constructor() : CrashReporter {
    override fun recordException(throwable: Throwable, attributes: Map<String, String>) = Unit
}
