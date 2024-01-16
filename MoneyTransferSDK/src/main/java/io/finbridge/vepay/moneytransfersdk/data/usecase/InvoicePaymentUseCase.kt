package io.finbridge.vepay.moneytransfersdk.data.usecase

import arrow.core.Either
import io.finbridge.vepay.moneytransfersdk.core.utils.ErrorInvoicePayment
import io.finbridge.vepay.moneytransfersdk.core.utils.NoInternetError
import io.finbridge.vepay.moneytransfersdk.core.utils.PaymentError
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString
import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.ResourceProvider
import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.checkForInternet
import io.finbridge.vepay.moneytransfersdk.data.models.network.AcsRedirect
import io.finbridge.vepay.moneytransfersdk.data.models.network.BrowserData
import io.finbridge.vepay.moneytransfersdk.data.models.network.CardRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.CardResponse
import io.finbridge.vepay.moneytransfersdk.data.models.network.HeaderMap
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentResponse
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.Card
import io.finbridge.vepay.moneytransfersdk.data.repository.PaymentRepository
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Calendar
import java.util.Collections
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InvoicePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val resourceProvider: ResourceProvider,
) {
    suspend fun pay(
        id: String,
        card: Card,
        screenHeight: Int,
        screenWidth: Int,
    ): Either<ErrorInvoicePayment, PaymentResponse> {
        // return testAnswer()  //TODO удалить метод и его вызов
        if (checkForInternet(resourceProvider.getContext())) {
            paymentRepository.createPayment(
                id = id,
                paymentRequest = getPaymentRequest(
                    card = card,
                    screenHeight = screenHeight,
                    screenWidth = screenWidth
                )
            ).let { paymentResponse ->
                if (paymentResponse is Response.Success) {
                    return Either.Right(paymentResponse.result)
                } else {
                    return Either.Left(PaymentError)
                }
            }
        } else {
            return Either.Left(NoInternetError)
        }
    }

    private fun getPaymentRequest(
        card: Card,
        screenHeight: Int,
        screenWidth: Int,
    ): PaymentRequest {
        return PaymentRequest(
            ip = getIPAddress(true) ?: "",
            card = CardRequest(
                cardNumber = card.cardNumber?.replace(" ", emptyString()) ?: emptyString(),
                cardHolder = "CARD HOLDER",
                expires = card.expireDate?.replace("/", emptyString()) ?: emptyString(),
                cvc = card.cvv ?: emptyString()
            ),
            headerMap = HeaderMap(
                userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36",
                accept = "text/html",
                acceptLanguage = "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
            ),
            browserData = BrowserData(
                screenHeight = screenHeight,
                screenWidth = screenWidth,
                windowHeight = screenHeight,
                windowWidth = screenWidth,
                timezoneOffset = getTimezoneOffset(),
                language = resourceProvider.getContext().resources.configuration?.locales?.get(0)?.language
                    ?: "ru-RU",
                colorDepth = 24,
                javaEnabled = true,
            )
        )
    }

    private fun getTimezoneOffset(): Int {
        val calendar: Calendar = Calendar.getInstance(
            TimeZone.getTimeZone("GMT"),
            Locale.getDefault()
        )
        return calendar.time.timezoneOffset
    }

    private fun testAnswer(): Either.Right<PaymentResponse> {
        return Either.Right(
            PaymentResponse(
                acsRedirect = AcsRedirect(
                    status = "OK",
                    url = "https://xn--90aggfb2aw2a.xn--p1ai/",
                    method = null,
                    postParameters = null
                ),
                CardResponse(
                    cardNumber = emptyString(),
                    cardHolder = emptyString(),
                    expires = emptyString(),
                )
            )
        )
    }

    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = (sAddr?.indexOf(':') ?: 0) < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr?.indexOf('%') ?: 0
                                return if (delim < 0) sAddr?.uppercase(Locale.getDefault()) else sAddr?.substring(
                                    0,
                                    delim
                                )?.uppercase(
                                    Locale.getDefault()
                                )
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return ""
    }
}
