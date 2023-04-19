package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardRequest(
    @SerialName("cardNumber") val cardNumber: String,
    @SerialName("cardHolder") val cardHolder: String,
    @SerialName("expires") val expires: String,
    @SerialName("cvc") val cvc: String,
)
