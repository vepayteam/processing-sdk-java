package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("bank") val bank: String?,
    @SerialName("errorInfo") val errorInfo: String?,
    @SerialName("bankErrorCode") val bankErrorCode: String?,
)
