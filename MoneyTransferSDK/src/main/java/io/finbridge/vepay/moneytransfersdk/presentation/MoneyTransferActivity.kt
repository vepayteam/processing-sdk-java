package io.finbridge.vepay.moneytransfersdk.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.CardPayFragment

class MoneyTransferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_transfer)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CardPayFragment.newInstance())
                .commit()
        }
    }

}