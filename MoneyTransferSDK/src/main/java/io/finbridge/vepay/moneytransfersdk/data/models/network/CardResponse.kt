package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class CardResponse(
    @SerialName("cardNumber") val cardNumber: String,
    @SerialName("cardHolder") val cardHolder: String,
    @SerialName("expires") val expires: String,
)
