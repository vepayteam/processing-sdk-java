package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Error(
    @SerialName("name") val name: String?,
    @SerialName("message") val errorMessage: String?,
    @SerialName("code") val code: Int?,
    @SerialName("status") val status: Int?,
): RuntimeException()
