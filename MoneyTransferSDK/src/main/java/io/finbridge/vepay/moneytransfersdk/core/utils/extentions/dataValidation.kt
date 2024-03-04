package io.finbridge.vepay.moneytransfersdk.core.utils.extentions

fun dataValidation(month: Int?, year: Int?): String? {
    return if (month != null && year != null) {
        val resultMonth = if (month.toString().length == 1) "0$month"
            else month.toString()
        val resultYear = if (year.toString().length == 4) "${year - 2000}" else year.toString()
        "$resultMonth$resultYear"
    } else null
}
