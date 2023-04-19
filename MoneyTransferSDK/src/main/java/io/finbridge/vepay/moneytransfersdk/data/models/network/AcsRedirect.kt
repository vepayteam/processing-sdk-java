package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcsRedirect(
    @SerialName("status") val status: String?,
    @SerialName("url") val url: String?,
    @SerialName("method") val method: String?,
    @SerialName("postParameters") val postParameters: PostParameters?,
)
