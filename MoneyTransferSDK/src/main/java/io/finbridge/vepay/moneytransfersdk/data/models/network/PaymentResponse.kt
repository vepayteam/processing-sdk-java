package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    @SerialName("acsRedirect") val acsRedirect: AcsRedirect?,
    @SerialName("cardResponse") val cardResponse: CardResponse,
)
