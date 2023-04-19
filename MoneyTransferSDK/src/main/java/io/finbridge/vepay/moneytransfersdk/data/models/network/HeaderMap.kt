package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HeaderMap(
    @SerialName("userAgent") val userAgent: String,
    @SerialName("accept") val accept: String,
    @SerialName("acceptLanguage") val acceptLanguage: String?,
)
