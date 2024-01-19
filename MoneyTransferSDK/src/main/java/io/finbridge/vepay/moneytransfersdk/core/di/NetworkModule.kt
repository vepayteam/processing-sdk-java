package io.finbridge.vepay.moneytransfersdk.core.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.finbridge.vepay.moneytransfersdk.data.repository.InvoiceRepository
import io.finbridge.vepay.moneytransfersdk.data.repository.InvoiceRepositoryImpl
import io.finbridge.vepay.moneytransfersdk.data.repository.PaymentRepository
import io.finbridge.vepay.moneytransfersdk.data.repository.PaymentRepositoryImpl
import io.finbridge.vepay.moneytransfersdk.data.repository.RefundsPaymentRepository
import io.finbridge.vepay.moneytransfersdk.data.repository.RefundsPaymentRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import kotlinx.serialization.json.Json


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun getHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            followRedirects = true

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v(TAG_KTOR_LOGGER, message)
                    }

                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }

            engine {
                config {
                    connectTimeout(1, TimeUnit.MINUTES)
                    readTimeout(30, TimeUnit.SECONDS)
                    writeTimeout(30, TimeUnit.SECONDS)
                    hostnameVerifier(HostnameVerifier { hostname, session -> true })
                }
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                }
                header("Accept", "application/json")
                header("Content-Type", "application/json")
                header("Accept", "text/html")
                header("Content-Type", "text/html")
                header("Connection", "keep-alive")
                header("Accept-Encoding", "gzip, deflate")
            }
        }
    }

    @Provides
    fun getInvoiceRepository(invoiceRepository: InvoiceRepositoryImpl): InvoiceRepository =
        invoiceRepository

    @Provides
    fun getPaymentRepository(
        paymentRepository: PaymentRepositoryImpl,
    ): PaymentRepository =
        paymentRepository

    @Provides
    fun getRefundsPaymentRepository(refundsPaymentRepository: RefundsPaymentRepositoryImpl): RefundsPaymentRepository =
        refundsPaymentRepository

    companion object {
        private const val BASE_URL = "test.vepay.online/h2hapi/v1"
        private const val TAG_KTOR_LOGGER = "ktor_logger:"
    }
}
