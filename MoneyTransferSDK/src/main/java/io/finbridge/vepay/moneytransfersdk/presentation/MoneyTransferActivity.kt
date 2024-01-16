package io.finbridge.vepay.moneytransfersdk.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString
import io.finbridge.vepay.moneytransfersdk.data.models.TransferStatus
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.yourCard.YourCardFragment
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoneyTransferActivity : AppCompatActivity() {
    val viewModel: MoneyTransferViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_transfer)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                YourCardFragment.newInstance(intent.getStringExtra(UUID_KEY) ?: emptyString())
            ).commit()
        }

        lifecycleScope.launch {
            viewModel.paymentStatus.collect {
                if (it != TransferStatus.WAITING_CHECK_STATUS) {
                    val resultIntent = Intent()
                    resultIntent.putExtra(TRANSFER_STATUS_KEY, it)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    companion object {
        const val UUID_KEY = "uuid_key"
        const val TRANSFER_STATUS_KEY = "transfer_status_key"
    }
}
