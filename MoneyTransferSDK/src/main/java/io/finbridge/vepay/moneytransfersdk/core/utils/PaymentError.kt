package io.finbridge.vepay.moneytransfersdk.core.utils

import io.finbridge.vepay.moneytransfersdk.R

object PaymentError : ErrorInvoicePayment {

    override fun getErrorMessageResource(): Int {
        return R.string.invoice_payment_default_error
    }
}
