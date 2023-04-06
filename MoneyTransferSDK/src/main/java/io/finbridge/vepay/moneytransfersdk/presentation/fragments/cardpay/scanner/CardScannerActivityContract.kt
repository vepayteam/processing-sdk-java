package io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.scanner

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import io.card.payment.CardIOActivity

class CardScannerActivityContract : ActivityResultContract<Unit, Intent?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        val scanIntent = Intent(context, CardIOActivity::class.java)
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
        return scanIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? = intent
}
