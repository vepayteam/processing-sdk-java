package io.finbridge.vepay.android_app.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.finbridge.vepay.android_app.R
import io.finbridge.vepay.android_app.presentation.fragment.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}