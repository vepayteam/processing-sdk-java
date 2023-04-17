package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class PaymentResponse(
    @SerialName("acsRedirect") val acsRedirect: AcsRedirect?,
    @SerialName("cardResponse") val cardResponse: CardResponse,
)
