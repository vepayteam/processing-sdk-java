package io.finbridge.vepay.moneytransfersdk.api


import io.finbridge.vepay.moneytransfersdk.data.models.network.AcsRedirect
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import javax.inject.Inject

class GetThreeDsApi @Inject constructor(
    private val httpClient: HttpClient,
) : KtorApiRequest<AcsRedirect, String> {

    override suspend fun execute(
        acsRedirect: AcsRedirect,
    ): String {
        return httpClient.post("${acsRedirect.url}") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(HttpHeaders.ContentType, "html/text")
                        append(HttpHeaders.Accept, "html/text")
                        append("PaReq", "${acsRedirect.postParameters?.paReq}")
                        append("TermUrl", "${acsRedirect.postParameters?.termUrl}")
                        append("MD", "${acsRedirect.postParameters?.md}")
                    },
                )
            )
        }.body()
    }
}
