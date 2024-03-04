package io.finbridge.vepay.android_app.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.finbridge.vepay.android_app.R
import io.finbridge.vepay.moneytransfersdk.core.utils.MoneyTransferActivityContract
import io.finbridge.vepay.moneytransfersdk.data.models.InputParams
import io.finbridge.vepay.moneytransfersdk.data.models.TransferStatus

class MainActivity : AppCompatActivity() {
    private val initMoneyTransfer =
        registerForActivityResult(MoneyTransferActivityContract()) { transferStatus: TransferStatus? ->
            Toast.makeText(this, "$transferStatus", Toast.LENGTH_LONG).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMoneyTransfer.launch(InputParams(
            uuid = "df44ef10-3694-4438-9178-df3dcbb92ce9",
            xUser = "376"
        ))
    }
}
