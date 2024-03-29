package io.finbridge.vepay.moneytransfersdk.data.models.ui.card

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.parcelize.Parcelize

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
                return if (TextUtils.isEmpty(number) || number.length < 14) {
                    false
                } else {
                    var sum = 0
                    if (number.length % 2 == 0) {
                        for (i in number.indices step 2) {
                            var c = number.substring(i, i + 1).toInt()
                            c *= 2
                            if (c > 9) {
                                c -= 9
                            }
                            sum += c
                            sum += number.substring(i + 1, i + 2).toInt()
                        }
                    } else {
                        for (i in 1 until number.length step 2) {
                            var c: Int = number.substring(i, i + 1).toInt()
                            c *= 2
                            if (c > 9) {
                                c -= 9
                            }
                            sum += c
                            sum += number.substring(i - 1, i).toInt()
                        }
                    }
                    sum % 10 == 0
                }
            }
        }

        fun isValidDate(exp: String?): Boolean {
            return if (exp == null) {
                false
            } else {
                val expDate = exp.replace("/", "")
                if (expDate.length != 4) {
                    false
                } else {
                    val month = expDate.substring(0, 2).toInt()
                    return month in 1..12
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
