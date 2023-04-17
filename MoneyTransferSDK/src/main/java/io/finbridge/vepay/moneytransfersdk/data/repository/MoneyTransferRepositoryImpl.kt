package io.finbridge.vepay.moneytransfersdk.data.repository

import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceResponse
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import javax.inject.Inject

class MoneyTransferRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : MoneyTransferRepository {

    override suspend fun createInvoice(invoiceRequest: InvoiceRequest): Response<InvoiceResponse> {
        return try {
            Response.Success(httpClient.post(body = invoiceRequest) {
                url(CREATE_INVOICE_URL)
            })
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

    override suspend fun getInvoice(id: Int): Response<InvoiceResponse> {
        return try {
            Response.Success(httpClient.get(GET_INVOICE_URL) {
                parameter(ID_KEY, id)
            })
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

    override suspend fun createPayment(
        id: Int,
        paymentRequest: PaymentRequest,
    ): Response<PaymentResponse> {
        return try {
            Response.Success(httpClient.put(body = paymentRequest) {
                url(PAYMENTS_URL)
                parameter(ID_KEY, id)
            })
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

    override suspend fun getPayment(id: Int): Response<PaymentResponse> {
        return try {
            Response.Success(httpClient.get(PAYMENTS_URL) {
                parameter(ID_KEY, id)
            })
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

    override suspend fun refundsPayment(id: Int, amountFractional: Int): Response<String> {
        return try {
            Response.Success(httpClient.post(body = amountFractional) {
                url(REFUNDS_URL)
                parameter(ID_KEY, id)
            })
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

    override suspend fun getRefunds(id: Int): Response<String> {
        return try {
            Response.Success(httpClient.get(GET_REFUNDS_URL) {
                parameter(ID_KEY, id)
            })
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

    override suspend fun reversedPayment(id: Int): Response<String> {
        return try {
            Response.Success(httpClient.put() {
                url(REVERSED_URL)
                parameter(ID_KEY, id)
            })
        } catch (exception: Exception) {
            Response.Failure(exception)
        }
    }

    companion object {
        private const val ID_KEY = "id"
        private const val BASE_URL = "https://api.vepay.online/h2hapi/v1"
        private const val CREATE_INVOICE_URL = "$BASE_URL/invoices"
        private const val GET_INVOICE_URL = "$BASE_URL/invoices/{id}"
        private const val PAYMENTS_URL = "$BASE_URL/invoices/{id}/payments"
        private const val REFUNDS_URL = "$BASE_URL/invoices/{id}/payments/refunds"
        private const val GET_REFUNDS_URL = "$BASE_URL/refunds/{id}"
        private const val REVERSED_URL = "$BASE_URL/invoices/{id}/payments/reversed"
    }
}
