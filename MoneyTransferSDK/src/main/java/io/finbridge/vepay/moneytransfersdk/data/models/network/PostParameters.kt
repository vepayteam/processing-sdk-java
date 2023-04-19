package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostParameters(
    @SerialName("pa_req") val paReq: String? = null,
    @SerialName("md") val md: String? = null,
    @SerialName("term_url") val termUrl: String? = null,
)
