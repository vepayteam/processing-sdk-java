package io.finbridge.vepay.moneytransfersdk.api

import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getApiContentSafeOperation
import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceResponse
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class GetInvoiceApi @Inject constructor(
    private val httpClient: HttpClient,
) : KtorApiRequest<Int, Response<InvoiceResponse>> {

    override suspend fun execute(id: Int): Response<InvoiceResponse> {
        return getApiContentSafeOperation<InvoiceResponse> {
            httpClient.get("invoices/$id") {
                parameter("id", id)
            }.body()
        }
    }
}
