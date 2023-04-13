package io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.scanner

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.dataValidation
import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getSafetyParcelableExtra
import io.finbridge.vepay.moneytransfersdk.data.models.card.Card
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.cardscanners.ui.CardNfcScannerActivity

class CardScannerActivityContract() : ActivityResultContract<ScannerType, Card?>() {

    override fun createIntent(context: Context, input: ScannerType): Intent {
        return when (input) {
            ScannerType.CAMERA -> {
                val scanIntent = Intent(context, CardIOActivity::class.java)
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
                scanIntent
            }
            ScannerType.NFC -> {
                Intent(context, CardNfcScannerActivity::class.java)
            }
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Card? {
        var scannedCardData: Card? = null

        if (intent != null && intent.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            val scanResult = intent.getSafetyParcelableExtra(
                CardIOActivity.EXTRA_SCAN_RESULT,
                CreditCard::class.java,
            )
            scannedCardData = Card(
                cardNumber = scanResult?.cardNumber,
                expireDate = dataValidation(scanResult?.expiryMonth, scanResult?.expiryYear),
                cardholderName = scanResult?.cardholderName,
                cvv = scanResult?.cvv,
            )
        } else if (intent != null && intent.hasExtra(ScannerType.NFC.name)) {
            scannedCardData =
                intent.getSafetyParcelableExtra(ScannerType.NFC.name, Card::class.java)
        }
        return scannedCardData
    }
}
