package io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.cardscanners.ui

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.devnied.emvnfccard.model.EmvCard
import com.github.devnied.emvnfccard.parser.EmvTemplate
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.dataValidation
import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.getSafetyParcelableExtra
import io.finbridge.vepay.moneytransfersdk.data.models.card.Card
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.scanner.NFCProvider
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.scanner.ScannerType
import java.util.Date
import java.util.Calendar


class CardNfcScannerActivity : AppCompatActivity() {
    private var detectedTag: Tag? = null
    private var nfcAdapter: NfcAdapter? = null
    private var readTagFilters: Array<IntentFilter>? = null
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_nfc_scanner)

        initializeNfcReader()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            onResult(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, readTagFilters, NFC_TAGS_TYPES)
    }

    private fun initializeNfcReader() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        detectedTag = intent.getSafetyParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)

        pendingIntent = PendingIntent.getActivity(
            applicationContext, 0,
            Intent(
                this,
                CardNfcScannerActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        readTagFilters = arrayOf(tagDetected, filter)
    }

    private fun onResult(intent: Intent) {
        val emvCard = readFromTag(intent)
        val scannedCardData =
            emvCard?.let {
                Card(
                    cardNumber = it.cardNumber,
                    expireDate = convertData(it.expireDate),
                    cardholderName = "${it.holderFirstname} ${it.holderLastname}",
                    cvv = null
                )
            }
        val resultIntent = Intent()
        resultIntent.putExtra(ScannerType.NFC.name, scannedCardData)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun readFromTag(intent: Intent): EmvCard? {
        var card: EmvCard? = null
        val tag: Tag? = intent.getSafetyParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        val isoDep: IsoDep = IsoDep.get(tag)
        val provider = NFCProvider()
        provider.setTagCom(isoDep)
        val config: EmvTemplate.Config = EmvTemplate.Config()
            .setContactLess(true)
            .setReadAllAids(true)
            .setReadTransactions(true)
            .setReadCplc(false)
            .setRemoveDefaultParsers(false)
            .setReadAt(true)

        val parser = EmvTemplate.Builder()
            .setProvider(provider)
            .setConfig(config)
            .build()
        try {
            isoDep.connect()
            card = parser.readEmvCard()
            isoDep.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return card
    }

    private fun convertData(date: Date): String? {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        return dataValidation(
            calendar.get(Calendar.MONTH) + ADDING_MONTH,
            calendar.get(Calendar.YEAR)
        )
    }

    companion object {
        private const val ADDING_MONTH = 1
        private val NFC_TAGS_TYPES = arrayOf(
            arrayOf("android.nfc.tech.NfcA"),
            arrayOf("android.nfc.tech.NfcB"),
            arrayOf("android.nfc.tech.IsoDep")
        )
    }
}
