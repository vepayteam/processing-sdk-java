package io.finbridge.vepay.moneytransfersdk.api

import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getApiContentSafeOperation
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentResponse
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class GetPaymentApi @Inject constructor(
    private val httpClient: HttpClient,
) : KtorApiRequest<Int, Response<PaymentResponse>> {

    override suspend fun execute(id: Int): Response<PaymentResponse> {
        return getApiContentSafeOperation<PaymentResponse> {
            httpClient.get("invoices/$id/payments") {
                parameter("id", id)
            }.body()
        }
    }
}
