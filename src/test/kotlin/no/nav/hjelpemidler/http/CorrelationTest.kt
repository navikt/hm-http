package no.nav.hjelpemidler.http

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.request
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CorrelationTest {
    @Test
    fun `Gjeldende correlationId skal være satt`() {
        withCorrelationId {
            currentCorrelationId() shouldNotBe null
        }
    }

    @Test
    fun `Gjeldende correlationId skal settes som header`() = runTest {
        val engine = MockEngine {
            respondOk("test")
        }
        val client = createHttpClient(engine) {
            defaultRequest {
                correlationId()
            }
        }
        withCorrelationId {
            val response = client.get("/test")
            val request = response.request
            val id = currentCorrelationId().shouldNotBeNull()
            request.navCallId() shouldBe id
            request.navCorrelationId() shouldBe id
            request.navConsumerId() shouldBe "hm-http"
        }
    }

    @Test
    fun `Nestet correlationId`() {
        withCorrelationId {
            val id1 = currentCorrelationId()
            val id2 = withCorrelationId {
                currentCorrelationId()
            }
            id1 shouldBe id2
        }
    }
}
