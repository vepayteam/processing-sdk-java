package io.finbridge.vepay.moneytransfersdk.api.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


internal class KtorUtils {
    companion object {
        fun provideKtor(content: String): HttpClient {
            return HttpClient(MockEngine) {
                expectSuccess = true

                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        }
                    )
                }

                defaultRequest {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = BASE_URL
                    }
                    header("Accept", "application/json")
                    header("Content-Type", "application/json")
                    header("Connection", "keep-alive")
                    header("Accept-Encoding", "gzip, deflate, br")
                }

                engine {
                    addHandler {
                        respond(
                            content = content,
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }
                }
            }
        }

        private const val BASE_URL = "test.vepay.online/h2hapi/v1"
    }
}
