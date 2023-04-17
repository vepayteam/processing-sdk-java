package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class InvoiceResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("amountFractional") val amountFractional: Int,
    @SerialName("currencyCode") val currencyCode: String,
    @SerialName("documentId") val documentId: String?,
    @SerialName("externalId") val externalId: String,
    @SerialName("description") val description: String?,
    @SerialName("timeoutSeconds") val timeoutSeconds: Int?,
    @SerialName("successUrl") val successUrl: String?,
    @SerialName("failUrl") val failUrl: String?,
    @SerialName("cancelUrl") val cancelUrl: String?,
    @SerialName("client") val client: Client,
    @SerialName("status") val status: Status?,
)
