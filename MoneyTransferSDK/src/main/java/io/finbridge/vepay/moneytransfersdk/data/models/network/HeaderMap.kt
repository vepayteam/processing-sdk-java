package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class HeaderMap(
    @SerialName("userAgent") val userAgent: String,
    @SerialName("accept") val accept: String,
    @SerialName("acceptLanguage") val acceptLanguage: String?,
)
