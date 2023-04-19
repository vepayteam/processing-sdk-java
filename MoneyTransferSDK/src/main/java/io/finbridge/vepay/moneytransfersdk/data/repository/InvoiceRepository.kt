package io.finbridge.vepay.moneytransfersdk.data.repository

import io.finbridge.vepay.moneytransfersdk.api.CreateInvoiceApi
import io.finbridge.vepay.moneytransfersdk.api.GetInvoiceApi
import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceRequest
import io.finbridge.vepay.moneytransfersdk.data.models.network.InvoiceResponse
import javax.inject.Inject

interface InvoiceRepository {
    suspend fun createInvoice(invoiceRequest: InvoiceRequest): Response<InvoiceResponse>
    suspend fun getInvoice(id: Int): Response<InvoiceResponse>
}

class InvoiceRepositoryImpl @Inject constructor(
    private val createInvoiceApi: CreateInvoiceApi,
    private val getInvoiceApi: GetInvoiceApi,
) : InvoiceRepository {

    override suspend fun createInvoice(invoiceRequest: InvoiceRequest): Response<InvoiceResponse> {
        return createInvoiceApi.execute(invoiceRequest)
    }

    override suspend fun getInvoice(id: Int): Response<InvoiceResponse> {
        return getInvoiceApi.execute(id)
    }
}
