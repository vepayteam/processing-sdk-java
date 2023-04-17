package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class PostParameters(
    @SerialName("pa_req") val paReq: String? = null,
    @SerialName("md") val md: String? = null,
    @SerialName("term_url") val termUrl: String? = null,
)
