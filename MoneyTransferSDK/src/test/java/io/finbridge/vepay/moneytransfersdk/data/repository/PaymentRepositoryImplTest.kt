package io.finbridge.vepay.moneytransfersdk.data.repository

import io.finbridge.vepay.moneytransfersdk.api.CreatePaymentApi
import io.finbridge.vepay.moneytransfersdk.api.GetPaymentApi
import io.finbridge.vepay.moneytransfersdk.api.utils.DataMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PaymentRepositoryImplTest {
    private val createPaymentApi: CreatePaymentApi = mockk(relaxed = true)
    private val getPaymentApi: GetPaymentApi = mockk(relaxed = true)
    private val repository = PaymentRepositoryImpl(createPaymentApi, getPaymentApi)

    @Test
    fun createPayment() = runBlocking {
        val id = "id"
        val request = DataMocks.paymentRequest

        repository.createPayment(id, request)
        coVerify(exactly = 1, verifyBlock = { createPaymentApi.execute(id, request) })
    }
}
