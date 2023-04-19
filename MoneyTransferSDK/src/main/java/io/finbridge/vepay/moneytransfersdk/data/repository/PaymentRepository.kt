package io.finbridge.vepay.moneytransfersdk.data.repository

import io.finbridge.vepay.moneytransfersdk.api.CreatePaymentApi
import io.finbridge.vepay.moneytransfersdk.api.GetPaymentApi
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.PaymentResponse
import javax.inject.Inject

interface PaymentRepository {
    suspend fun createPayment(id: Int,paymentRequest: PaymentRequest): Response<PaymentResponse>
    suspend fun getPayment(id: Int): Response<PaymentResponse>
}

class PaymentRepositoryImpl @Inject constructor(
    private val createPaymentApi: CreatePaymentApi,
    private val getPaymentApi: GetPaymentApi,
) : PaymentRepository {
    override suspend fun createPayment(
        id: Int,
        paymentRequest: PaymentRequest,
    ): Response<PaymentResponse> {
       return createPaymentApi.execute(id,paymentRequest)
    }

    override suspend fun getPayment(id: Int): Response<PaymentResponse> {
        return getPaymentApi.execute(id)
    }
}
