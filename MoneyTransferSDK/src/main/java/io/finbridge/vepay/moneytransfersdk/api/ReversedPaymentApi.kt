package io.finbridge.vepay.moneytransfersdk.api

import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getApiContentSafeOperation
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import javax.inject.Inject

class ReversedPaymentApi @Inject constructor(
    private val httpClient: HttpClient,
) : KtorApiRequest<Int, Response<String>> {

    override suspend fun execute(id: Int): Response<String> {
        return getApiContentSafeOperation<String> {
            httpClient.put("/invoices/$id/payments/reversed") {
                parameter("id", id)
            }.body()
        }
    }
}
