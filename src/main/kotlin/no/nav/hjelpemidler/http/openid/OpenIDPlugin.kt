package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.coroutines.currentCoroutineContext

class OpenIDPluginConfiguration {
    var scope: String = ""
    val client: OpenIDClient? = null

    internal val clientConfiguration: OpenIDClientConfiguration = OpenIDClientConfiguration()

    fun client(block: OpenIDClientConfiguration.() -> Unit) {
        clientConfiguration.apply(block)
    }
}

val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    val scope = pluginConfig.scope
    val engine = client.engine
    val client = when (val client = pluginConfig.client) {
        null -> createOpenIDClient(
            engine = engine,
            configuration = pluginConfig.clientConfiguration,
        )

        else -> client
    }
    onRequest { request, _ ->
        val openIDContext = currentCoroutineContext().openIDContext()
        val tokenSet = when (val accessToken = openIDContext.accessToken) {
            null -> client.grant(scope = scope)
            else -> client.grant(scope = scope, onBehalfOf = accessToken)
        }
        request.bearerAuth(tokenSet)
    }
}

fun HttpClientConfig<*>.openID(block: OpenIDPluginConfiguration.() -> Unit) {
    install(OpenIDPlugin) {
        block()
    }
}

fun HttpClientConfig<*>.azureAD(scope: String, block: OpenIDClientConfiguration.() -> Unit) {
    install(OpenIDPlugin) {
        this.scope = scope
        client {
            azureADEnvironmentConfiguration()
            block()
        }
    }
}

fun HttpClientConfig<*>.tokenX(scope: String, block: OpenIDClientConfiguration.() -> Unit) {
    install(OpenIDPlugin) {
        this.scope = scope
        client {
            tokenXEnvironmentConfiguration()
            block()
        }
    }
}
