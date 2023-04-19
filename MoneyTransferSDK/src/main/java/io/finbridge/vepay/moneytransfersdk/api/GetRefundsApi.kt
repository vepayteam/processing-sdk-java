package io.finbridge.vepay.moneytransfersdk.api

import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getApiContentSafeOperation
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class GetRefundsApi @Inject constructor(
    private val httpClient: HttpClient,
) : KtorApiRequest<Int, Response<String>> {

    override suspend fun execute(id: Int): Response<String> {
        return getApiContentSafeOperation<String> {
            httpClient.get("/refunds/$id") {
                parameter("id", id)
            }.body()
        }
    }
}
