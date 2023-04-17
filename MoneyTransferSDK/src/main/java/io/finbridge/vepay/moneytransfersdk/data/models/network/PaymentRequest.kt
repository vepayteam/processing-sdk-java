package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class PaymentRequest(
    @SerialName("ip") val ip: String,
    @SerialName("card") val card: CardRequest,
    @SerialName("headerMap") val headerMap: HeaderMap?,
    @SerialName("browserData") val browserData: BrowserData? = null,
)
