package io.finbridge.vepay.moneytransfersdk.data.repository

import io.finbridge.vepay.moneytransfersdk.api.CreatePaymentApi
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentResponse
import javax.inject.Inject

interface PaymentRepository {
    suspend fun createPayment(
        id: String, xUser: String,
        paymentRequest: PaymentRequest
    ): Response<PaymentResponse>
}

class PaymentRepositoryImpl @Inject constructor(
    private val createPaymentApi: CreatePaymentApi,
) : PaymentRepository {
    override suspend fun createPayment(
        id: String,
        xUser: String,
        paymentRequest: PaymentRequest,
    ): Response<PaymentResponse> {
        return createPaymentApi.execute(id, xUser, paymentRequest)
    }
}
