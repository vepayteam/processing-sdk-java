package io.finbridge.vepay.android_app.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.finbridge.vepay.android_app.R
import io.finbridge.vepay.moneytransfersdk.presentation.MoneyTransferActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, MoneyTransferActivity::class.java)
        startActivity(intent)
    }
}