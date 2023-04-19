package io.finbridge.vepay.moneytransfersdk.api

import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getApiContentSafeOperation
import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceResponse
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class CreateInvoiceApi @Inject constructor(
    private val httpClient: HttpClient,
) : KtorApiRequest<InvoiceRequest, Response<InvoiceResponse>> {

    override suspend fun execute(invoiceRequest: InvoiceRequest): Response<InvoiceResponse> {
        return getApiContentSafeOperation<InvoiceResponse> {
            httpClient.post("/invoices") {
                setBody(invoiceRequest)
            }.body()
        }
    }
}
