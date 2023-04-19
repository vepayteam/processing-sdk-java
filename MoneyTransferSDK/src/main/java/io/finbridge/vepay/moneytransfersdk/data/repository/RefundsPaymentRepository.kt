package io.finbridge.vepay.moneytransfersdk.data.repository

import io.finbridge.vepay.moneytransfersdk.api.GetRefundsApi
import io.finbridge.vepay.moneytransfersdk.api.RefundsPaymentApi
import io.finbridge.vepay.moneytransfersdk.api.ReversedPaymentApi
import javax.inject.Inject

interface RefundsPaymentRepository {
    suspend fun refundsPayment(id: Int, amountFractional: Int): Response<String>
    suspend fun getRefunds(id: Int): Response<String>
    suspend fun reversedPayment(id: Int): Response<String>
}

class RefundsPaymentRepositoryImpl @Inject constructor(
    private val refundsPaymentApi: RefundsPaymentApi,
    private val getRefundsApi: GetRefundsApi,
    private val reversedPaymentApi: ReversedPaymentApi,
) : RefundsPaymentRepository {
    override suspend fun refundsPayment(id: Int, amountFractional: Int): Response<String> {
        return refundsPaymentApi.execute(id, amountFractional)
    }

    override suspend fun getRefunds(id: Int): Response<String> {
        return getRefundsApi.execute(id)
    }

    override suspend fun reversedPayment(id: Int): Response<String> {
        return reversedPaymentApi.execute(id)
    }
}
