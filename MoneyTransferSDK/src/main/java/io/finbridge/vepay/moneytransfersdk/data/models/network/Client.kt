package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class Client(
  @SerialName("fullName") val fullName: String?,
  @SerialName("address") val address: String?,
  @SerialName("email") val email: String?,
  @SerialName("login") val login: String?,
  @SerialName("phone") val phone: String?,
  @SerialName("zip") val zip: String?,
)
