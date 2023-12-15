package io.finbridge.vepay.moneytransfersdk.api.api

import io.finbridge.vepay.moneytransfersdk.api.GetPaymentApi
import io.finbridge.vepay.moneytransfersdk.api.utils.DataMocks
import io.finbridge.vepay.moneytransfersdk.api.utils.GetPaymentResponse
import io.finbridge.vepay.moneytransfersdk.api.utils.KtorUtils
import io.finbridge.vepay.moneytransfersdk.data.repository.Response
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class GetPaymentApiTest {
    private val getPaymentApiTest =
        GetPaymentApi(KtorUtils.provideKtor(GetPaymentResponse))

    @Test
    fun execute() {
        runBlocking {
            val expectResponse = DataMocks.paymentResponse
            val actualResponse = getPaymentApiTest.execute("id")

            Assert.assertTrue(expectResponse == (actualResponse as Response.Success).result)
        }
    }
}
