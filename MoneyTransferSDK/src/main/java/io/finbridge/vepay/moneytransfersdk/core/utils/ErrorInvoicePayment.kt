package io.finbridge.vepay.moneytransfersdk.core.utils

import io.finbridge.vepay.moneytransfersdk.R

interface ErrorInvoicePayment {
    fun getErrorMessageResource(): Int = R.string.invoice_payment_default_error
}
