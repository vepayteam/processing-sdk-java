package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostParameters(
    @SerialName("PaReq") val paReq: String? = null,
    @SerialName("MD") val md: String? = null,
    @SerialName("TermUrl") val termUrl: String? = null,
)
