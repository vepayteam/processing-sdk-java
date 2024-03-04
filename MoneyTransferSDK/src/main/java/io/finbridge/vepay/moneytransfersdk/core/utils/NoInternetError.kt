package io.finbridge.vepay.moneytransfersdk.core.utils

import io.finbridge.vepay.moneytransfersdk.R


object NoInternetError : ErrorInvoicePayment {

    override fun getErrorMessageResource(): Int {
        return R.string.invoice_payment_no_internet_error
    }
}
