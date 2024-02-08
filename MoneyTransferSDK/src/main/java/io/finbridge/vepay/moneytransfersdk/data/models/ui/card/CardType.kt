package io.finbridge.vepay.moneytransfersdk.data.models.ui.card

import io.finbridge.vepay.moneytransfersdk.R


enum class CardType {
    UNKNOWN,
    VISA,
    MASTERCARD,
    MIR,
    JCB;

    companion object {

        fun getType(cardNumber: String?): CardType {
            if (cardNumber == null || cardNumber.isEmpty()) return UNKNOWN
            val first = Integer.valueOf(cardNumber.substring(0, 1))
            if (first == 4) return VISA
            if (cardNumber.length < 2) return UNKNOWN
            val firstTwo = Integer.valueOf(cardNumber.substring(0, 2))
            if (firstTwo == 35) return JCB
            if (firstTwo in 51..55) return MASTERCARD
            if (cardNumber.length < 4) return UNKNOWN
            val firstFour = Integer.valueOf(cardNumber.substring(0, 4))
            if (firstFour in 2200..2204) return MIR
            return if (firstFour in 2221..2720) MASTERCARD else UNKNOWN
        }
    }

    fun getIconRes(): Int? = when (this) {
        VISA -> R.drawable.ic_visa_color_40_24
        MASTERCARD -> R.drawable.ic_mastercard_40_24
        MIR -> R.drawable.ic_mir_40dp_24dp
        JCB -> R.drawable.ic_color_jcb_26_24
        else -> null
    }

    fun getCardInfoIconRes(): Int? = when (this) {
        VISA -> R.drawable.ic_visa_40_24
        MASTERCARD -> R.drawable.ic_mastercard_40_24
        MIR -> R.drawable.ic_mir
        JCB -> R.drawable.ic_jcb
        else -> null
    }
}
