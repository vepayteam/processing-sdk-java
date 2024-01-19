package io.finbridge.vepay.android_app.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.finbridge.vepay.android_app.R
import io.finbridge.vepay.moneytransfersdk.core.utils.MoneyTransferActivityContract
import io.finbridge.vepay.moneytransfersdk.data.models.TransferStatus

class MainActivity : AppCompatActivity() {
    private val initMoneyTransfer =
        registerForActivityResult(MoneyTransferActivityContract()) { transferStatus: TransferStatus? ->
            Toast.makeText(this, "$transferStatus", Toast.LENGTH_LONG).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMoneyTransfer.launch("7e1c78df-5017-4a3f-891d-af9ce1a774ff")
    }
}
