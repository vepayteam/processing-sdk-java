package io.finbridge.vepay.moneytransfersdk.data.models.ui.card

import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString

enum class CardBank {
    UNKNOWN,
    ALFA_BANK,
    SBER_BANK,
    TINKOFF_BANK,
    RAIFFEISEN_BANK,
    ADD_CARD;

    companion object {

        fun getType(cardNumber: String?): CardBank {
            if (cardNumber.isNullOrEmpty()) return UNKNOWN
            if (cardNumber.length > 5) {
                val correctCardNumber = cardNumber.replace(" ", emptyString())
                return when (Integer.valueOf(correctCardNumber.substring(0, 5))) {
                    52117, 54867, 54860, 45841, 41542, 67637, 47796, 51548 -> ALFA_BANK

                    42762, 42768, 63900, 67758, 42790, 54693, 42764,42766, 42760, 42763 -> SBER_BANK

                    52132, 43777 -> TINKOFF_BANK

                    46273, 46272 -> RAIFFEISEN_BANK

                    else -> UNKNOWN
                }
            } else {
                return UNKNOWN
            }
        }
    }

    fun getIconRes(): Int? = when (this) {
        UNKNOWN -> R.drawable.ic_error
        ALFA_BANK -> R.drawable.ic_alfa
        SBER_BANK -> R.drawable.ic_sber
        TINKOFF_BANK -> R.drawable.ic_tinkoff
        RAIFFEISEN_BANK -> R.drawable.ic_raif
        ADD_CARD -> R.drawable.ic_add
        else -> null
    }

    fun getLogoRes(): Int = when (this) {
        UNKNOWN -> R.drawable.ic_logo_unknow
        ALFA_BANK -> R.drawable.ic_logo_alfa
        SBER_BANK -> R.drawable.ic_sber_logo
        TINKOFF_BANK -> R.drawable.ic_logo_tinkoff
        RAIFFEISEN_BANK -> R.drawable.ic_logo_raiffeisen
        ADD_CARD -> R.drawable.ic_logo_unknow
        else -> R.drawable.ic_logo_unknow
    }

    fun getNameRes():Int = when (this) {
        UNKNOWN -> R.string.yourCard_Fragment_un_bank
        ALFA_BANK -> R.string.yourCard_Fragment_alfa_bank_label
        SBER_BANK -> R.string.yourCard_Fragment_sber_bank_label
        TINKOFF_BANK -> R.string.yourCard_Fragment_tinkoff_bank_label
        RAIFFEISEN_BANK -> R.string.yourCard_Fragment_raiff_bank_label
        ADD_CARD -> R.string.yourCard_Fragment_input_card_data_label
        else -> R.string.yourCard_Fragment_input_card_data_label
    }
}
