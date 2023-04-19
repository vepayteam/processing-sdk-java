package io.finbridge.vepay.moneytransfersdk.api

import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getApiContentSafeOperation
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class RefundsPaymentApi @Inject constructor(
    private val httpClient: HttpClient,
) : KtorApiParameterRequest<Int, Int, Response<String>> {

    override suspend fun execute(
        id: Int,
        amountFractional: Int,
    ): Response<String> {
        return getApiContentSafeOperation<String> {
            httpClient.post("/invoices/$id/payments/refunds") {
                parameter("id", id)
                setBody(amountFractional)
            }.body()
        }
    }
}
