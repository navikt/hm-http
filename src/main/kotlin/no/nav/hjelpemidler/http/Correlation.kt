package no.nav.hjelpemidler.http

import io.ktor.client.request.header
import io.ktor.http.HttpMessage
import io.ktor.http.HttpMessageBuilder
import mu.withLoggingContext
import no.nav.hjelpemidler.configuration.NaisEnvironmentVariable
import org.slf4j.MDC
import java.util.UUID

const val CORRELATION_ID_KEY = "correlationId"

internal const val NAV_CONSUMER_ID_KEY = "Nav-Consumer-Id"
internal const val NAV_CALL_ID_KEY = "Nav-CallId"
internal const val NAV_CORRELATION_ID_KEY = "X-Correlation-ID"

fun currentCorrelationId(): String =
    MDC.get(CORRELATION_ID_KEY) ?: UUID.randomUUID().toString()

fun HttpMessageBuilder.navConsumerId(value: String = NaisEnvironmentVariable.NAIS_APP_NAME) =
    header(NAV_CONSUMER_ID_KEY, value)

fun HttpMessageBuilder.navCallId(value: String) =
    header(NAV_CALL_ID_KEY, value)

fun HttpMessageBuilder.navCorrelationId(value: String) =
    header(NAV_CORRELATION_ID_KEY, value)

fun HttpMessage.navConsumerId(): String? =
    headers[NAV_CONSUMER_ID_KEY]

fun HttpMessage.navCallId(): String? =
    headers[NAV_CALL_ID_KEY]

fun HttpMessage.navCorrelationId(): String? =
    headers[NAV_CORRELATION_ID_KEY]

fun HttpMessageBuilder.correlationId(value: String = currentCorrelationId()): String {
    navCallId(value)
    navCorrelationId(value)
    navConsumerId()
    return value
}

inline fun <T> withCorrelationId(
    vararg pair: Pair<String, String?>,
    restorePrevious: Boolean = true,
    body: () -> T,
): T =
    withLoggingContext(
        pair = arrayOf(Pair<String, String?>(CORRELATION_ID_KEY, currentCorrelationId())).plus(pair),
        restorePrevious = restorePrevious,
        body = body,
    )
