package io.finbridge.vepay.moneytransfersdk.api.utils

import io.finbridge.vepay.moneytransfersdk.data.models.network.AcsRedirect
import io.finbridge.vepay.moneytransfersdk.data.models.network.BrowserData
import io.finbridge.vepay.moneytransfersdk.data.models.network.CardRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.CardResponse
import io.finbridge.vepay.moneytransfersdk.data.models.network.HeaderMap
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentResponse
import io.finbridge.vepay.moneytransfersdk.data.models.network.PostParameters

object DataMocks {
    val paymentResponse = PaymentResponse(
        acsRedirect = AcsRedirect(
            status = "OK",
            url = "https://some-access-control-server.com/3ds?payId=123",
            method = "POST",
            postParameters = PostParameters(
                paReq = null,
                md = null,
                termUrl = null
            )
        ),
        cardResponse = CardResponse(
            cardNumber = "4111111111111111",
            cardHolder = "Terentiev Mihail",
            expires = "0122"
        ),
    )

    val paymentRequest = PaymentRequest(
        ip = "108.177.16.21",
        card = CardRequest(
            cardNumber = "4917610000000000",
            cardHolder = "Terentiev Mihail",
            expires = "0122",
            cvc = "123"
        ),
        headerMap = HeaderMap(
            userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36",
            accept = "text/html",
            acceptLanguage = "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7"
        ),
        browserData = BrowserData(
            screenHeight = 800,
            screenWidth = 1200,
            windowHeight = 800,
            windowWidth = 1200,
            timezoneOffset = -180,
            language = "ru-RU",
            colorDepth = 24,
            javaEnabled = false
        )
    )
}
