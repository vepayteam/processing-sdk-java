package io.finbridge.vepay.moneytransfersdk.api

import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getApiContentSafeOperation
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentResponse
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import javax.inject.Inject

class CreatePaymentApi @Inject constructor(
    private val httpClient: HttpClient,
) : KtorApiParameterRequest<Int, PaymentRequest, Response<PaymentResponse>> {

    override suspend fun execute(
        id: Int,
        paymentRequest: PaymentRequest,
    ): Response<PaymentResponse> {
        return getApiContentSafeOperation<PaymentResponse> {
            httpClient.put("/invoices/$id/payments") {
                parameter("id", id)
                setBody(paymentRequest)
            }.body()
        }
    }
}
