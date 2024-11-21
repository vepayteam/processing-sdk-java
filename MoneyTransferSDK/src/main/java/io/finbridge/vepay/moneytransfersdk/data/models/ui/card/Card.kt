package io.finbridge.vepay.moneytransfersdk.data.models.ui.card

import android.os.Build
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.Calendar

@Parcelize
data class Card(
    val cardNumber: String?,
    val expireDate: String?,
    val cardholderName: String?,
    val cvv: String?,
) : Parcelable {
    companion object {
        fun isValidNumber(cardNumber: String?): Boolean {
            return if (cardNumber == null) {
                false
            } else {
                val number = prepareCardNumber(cardNumber)
                if (number.isEmpty() || number.length < 14 || number.length > 19) {
                    false
                } else {
                    var sum = 0
                    val length = number.length
                    for (i in 0 until length) {
                        var digit = number[length - 1 - i].toString().toInt()
                        if (i % 2 == 1) {
                            digit *= 2
                            if (digit > 9) {
                                digit -= 9
                            }
                        }
                        sum += digit
                    }
                    sum % 10 == 0
                }
            }
        }

        fun isValidDate(exp: String?, cardType: CardType?): Boolean {
            return if (exp == null) {
                false
            } else {
                val expDate = exp.replace("/", "")
                if (expDate.length != 4) {
                    false
                } else {
                    val month = expDate.substring(0, 2).toInt()
                    val year = expDate.substring(2, 4).toInt()
                    return if (month in 1..12 && cardType != CardType.MIR) {
                        true
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            year + 2000 > LocalDate.now().year || (month >= LocalDate.now().monthValue && year + 2000 == LocalDate.now().year)
                        } else {
                            val calendar: Calendar = Calendar.getInstance()
                            val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
                            val currentYear = calendar[Calendar.YEAR]
                            return year + 2000 > currentYear || (month >= currentMonth && year + 2000 == currentYear)
                        }
                    }
                }
            }
        }

        fun isValidCvv(cvv: String?): Boolean {
            return (cvv?.length in 3..3)
        }

        fun isValidCardHolder(cardHolder: String?): Boolean {
            var result = false
            if (cardHolder != null) {
                if (cardHolder.length > 5 && cardHolder.contains(" ")) result = true
            }
            return result
        }

        private fun prepareCardNumber(cardNumber: String): String {
            return cardNumber.replace("\\s".toRegex(), "")
        }
    }
}
