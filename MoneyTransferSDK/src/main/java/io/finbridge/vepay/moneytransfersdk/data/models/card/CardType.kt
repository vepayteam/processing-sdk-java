package io.finbridge.vepay.moneytransfersdk.data.models.card

import io.finbridge.vepay.moneytransfersdk.R


enum class CardType {
	UNKNOWN,
	VISA,
	MASTERCARD,
	MAESTRO,
	MIR,
	JCB;

	companion object {

		fun getType(cardNumber: String?): CardType {
			if (cardNumber == null || cardNumber.isEmpty()) return UNKNOWN
			val first = Integer.valueOf(cardNumber.substring(0, 1))
			if (first == 4) return VISA
			if (first == 6) return MAESTRO
			if (cardNumber.length < 2) return UNKNOWN
			val firstTwo = Integer.valueOf(cardNumber.substring(0, 2))
			if (firstTwo == 35) return JCB
			if (firstTwo == 50 || (firstTwo in 56..58)) return MAESTRO
			if (firstTwo in 51..55) return MASTERCARD
			if (cardNumber.length < 4) return UNKNOWN
			val firstFour = Integer.valueOf(cardNumber.substring(0, 4))
			if (firstFour in 2200..2204) return MIR
			return if (firstFour in 2221..2720) MASTERCARD else UNKNOWN
		}
	}

	fun getIconRes(): Int? = when (this) {
		VISA -> R.drawable.ic_ps_visa_24dp
		MASTERCARD -> R.drawable.ic_ps_mastercard_24dp
		MAESTRO -> R.drawable.ic_ps_maestro_24dp
		MIR -> R.drawable.ic_ps_mir_24dp
		JCB -> R.drawable.ic_ps_jcb_24dp
		else -> null
	}
}
