package io.finbridge.vepay.moneytransfersdk.data.models.network

import kotlinx.serialization.SerialName

data class BrowserData(
    @SerialName("screenHeight") val screenHeight: Int?,
    @SerialName("screenWidth") val screenWidth: Int?,
    @SerialName("windowHeight") val windowHeight: Int?,
    @SerialName("windowWidth") val windowWidth: Int?,
    @SerialName("timezoneOffset") val timezoneOffset: Int?,
    @SerialName("language") val language: String?,
    @SerialName("colorDepth") val colorDepth: Int?,
    @SerialName("javaEnabled") val javaEnabled: Boolean?,
)
