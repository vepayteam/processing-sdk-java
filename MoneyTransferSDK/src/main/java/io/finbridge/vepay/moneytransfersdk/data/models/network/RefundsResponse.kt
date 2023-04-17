package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class RefundsResponse(
    @SerialName("amountFractional") val amountFractional: Int?,
    @SerialName("id") val id: Int?,
    @SerialName("status") val status: Int?,
    @SerialName("message") val message: String?,
)
