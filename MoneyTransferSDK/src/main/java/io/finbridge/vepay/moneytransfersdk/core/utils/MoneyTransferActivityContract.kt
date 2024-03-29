package io.finbridge.vepay.moneytransfersdk.core.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getSafetySerializableExtra
import io.finbridge.vepay.moneytransfersdk.data.models.InputParams
import io.finbridge.vepay.moneytransfersdk.data.models.TransferStatus
import io.finbridge.vepay.moneytransfersdk.presentation.MoneyTransferActivity

class MoneyTransferActivityContract() : ActivityResultContract<InputParams, TransferStatus?>() {

    override fun createIntent(context: Context, input: InputParams): Intent {
        val moneyTransferIntent = Intent(context, MoneyTransferActivity::class.java)
        moneyTransferIntent.putExtra(MoneyTransferActivity.UUID_KEY, input.uuid)
        moneyTransferIntent.putExtra(MoneyTransferActivity.XUSER_KEY, input.xUser)
        return moneyTransferIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): TransferStatus? {
        var result: TransferStatus? = null
        if (intent != null && intent.hasExtra(MoneyTransferActivity.TRANSFER_STATUS_KEY)) {
            result = intent.getSafetySerializableExtra(MoneyTransferActivity.TRANSFER_STATUS_KEY)
        }
        return result
    }
}
