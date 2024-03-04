package io.finbridge.vepay.moneytransfersdk.api.api

import io.finbridge.vepay.moneytransfersdk.api.CreatePaymentApi
import io.finbridge.vepay.moneytransfersdk.api.utils.DataMocks
import io.finbridge.vepay.moneytransfersdk.api.utils.CreatePaymentResponse
import io.finbridge.vepay.moneytransfersdk.api.utils.KtorUtils
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class CreatePaymentApiTest {
    private val getCreatePaymentApiTest =
        CreatePaymentApi(KtorUtils.provideKtor(CreatePaymentResponse))

    @Test
    fun execute() {
        runBlocking {
            val expectResponse = DataMocks.paymentResponse
            val actualResponse = getCreatePaymentApiTest.execute("id","123", DataMocks.paymentRequest)

            Assert.assertTrue(expectResponse == (actualResponse as Response.Success).result)
        }
    }
}
