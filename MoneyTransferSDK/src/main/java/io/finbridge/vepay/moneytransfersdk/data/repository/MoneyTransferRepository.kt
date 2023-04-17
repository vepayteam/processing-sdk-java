package io.finbridge.vepay.moneytransfersdk.data.repository

import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceResponse
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentResponse

interface MoneyTransferRepository {
    suspend fun createInvoice(invoiceRequest: InvoiceRequest): Response<InvoiceResponse>
    suspend fun getInvoice(id: Int): Response<InvoiceResponse>
    suspend fun createPayment(id: Int,paymentRequest: PaymentRequest): Response<PaymentResponse>
    suspend fun getPayment(id: Int): Response<PaymentResponse>
    suspend fun refundsPayment(id: Int, amountFractional: Int): Response<String>
    suspend fun getRefunds(id: Int): Response<String>
    suspend fun reversedPayment(id: Int): Response<String>
}
