package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefundsResponse(
    @SerialName("amountFractional") val amountFractional: Int?,
    @SerialName("id") val id: Int?,
    @SerialName("status") val status: Int?,
    @SerialName("message") val message: String?,
)
