package io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.Card
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.CardType
import io.finbridge.vepay.moneytransfersdk.databinding.FragmentCardPayBinding
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.scanner.CardScannerActivityContract
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.scanner.ScannerType
import ru.tinkoff.decoro.MaskDescriptor
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.DescriptorFormatWatcher

@AndroidEntryPoint
class CardPayFragment : Fragment() {

    private lateinit var binding: FragmentCardPayBinding
    private val viewModel: CardPayViewModel by viewModels()
    private val getCardDataFromScanner =
        registerForActivityResult(CardScannerActivityContract()) { scannedCardData: Card? ->
            if (scannedCardData == null) showErrorScanningToast()
            else putDataFromCardScanner(scannedCardData)
        }

    private val cardNumberFormatWatcher by lazy {
        val descriptor = MaskDescriptor.ofRawMask(CARD_NUMBER_MASK).setTerminated(true)
            .setForbidInputWhenFilled(true)

        DescriptorFormatWatcher(UnderscoreDigitSlotsParser(), descriptor)
    }

    private val cardDateFormatWatcher by lazy {
        val descriptor = MaskDescriptor.ofRawMask(CARD_DATE_MASK).setTerminated(true)
            .setForbidInputWhenFilled(true)

        DescriptorFormatWatcher(UnderscoreDigitSlotsParser(), descriptor)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCardPayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardNumberFormatWatcher.installOn(binding.editCardNumber)
        cardDateFormatWatcher.installOn(binding.editCardDate)

        initClickListeners()
        initDoAfterTextChanged()
        initOnFocusChangeListeners()
    }

    private fun initClickListeners() {
        binding.buttonPay.setOnClickListener {
            if (isValid()) {
                //TODO платёж
            }
        }

        binding.btnScan.setOnClickListener {
            if (isNfcEnable()) openScanTypeDialog()
            else getCardDataFromScanner.launch(ScannerType.CAMERA)
        }
    }

    private fun initOnFocusChangeListeners() {
        binding.editCardCvv.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidCvv(binding.editCardCvv.text.toString()),
                binding.editCardCvv
            )
        }
        binding.editCardDate.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidDate(binding.editCardDate.text.toString()),
                binding.editCardDate
            )
        }
        binding.editCardHolder.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidCardHolder(binding.editCardHolder.text.toString()),
                binding.editCardHolder
            )
        }
        binding.editCardNumber.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidNumber(binding.editCardNumber.text.toString()),
                binding.editCardNumber
            )
        }
    }

    private fun initDoAfterTextChanged() {
        binding.editCardNumber.doAfterTextChanged { editableText ->
            val cardNumber = editableText.toString()
            if (Card.isValidNumber(cardNumber)) {
                errorMode(false, binding.editCardNumber)
            } else {
                errorMode(cardNumber.length == CARD_NUMBER_MAX_LENGTH, binding.editCardNumber)
            }

            updatePaymentSystemIcon(cardNumber)
        }

        binding.editCardDate.doAfterTextChanged { editableText ->
            val cardExp = editableText.toString()
            if (Card.isValidDate(cardExp)) {
                binding.editCardHolder.requestFocus()
                errorMode(false, binding.editCardDate)
            }
        }

        binding.editCardHolder.doAfterTextChanged { editableText ->
            val cardHolder = editableText.toString()
            if (Card.isValidCardHolder(cardHolder)) {
                errorMode(false, binding.editCardHolder)
            }
        }

        binding.editCardCvv.doAfterTextChanged { editableText ->
            errorMode(false, binding.editCardCvv)

            if (Card.isValidCvv(editableText.toString())) {
                val inputMethodManager =
                    activity?.let { it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager }
                inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
                binding.editCardCvv.clearFocus()
            }
        }
    }

    private fun putDataFromCardScanner(scannedCardData: Card?) {
        binding.editCardNumber.setText(scannedCardData?.cardNumber)
        binding.editCardDate.setText(scannedCardData?.expireDate)
    }

    private fun errorMode(isErrorMode: Boolean, editText: TextInputEditText) {
        if (isErrorMode) {
            context?.let { editText.setTextColor(ContextCompat.getColor(it, R.color.pale_red)) }
            editText.setBackgroundResource(R.drawable.edit_text_underline_error)
        } else {
            context?.let {
                editText.setTextColor(ContextCompat.getColor(it, R.color.light_blue_grey))
            }
            editText.setBackgroundResource(R.drawable.edit_text_underline)
        }
    }

    private fun updatePaymentSystemIcon(cardNumber: String) {
        val cardType = CardType.getType(cardNumber)
        val psIcon = cardType.getIconRes()
        if (cardNumber.isEmpty() || psIcon == null) {
            binding.icPs.isVisible = false
            binding.btnScan.isVisible = true
        } else {
            binding.icPs.isVisible = true
            binding.btnScan.isVisible = false
            binding.icPs.setImageResource(psIcon)
        }
    }

    private fun isValid(): Boolean {
        val cardNumber = binding.editCardNumber.text.toString()
        val cardNumberIsValid = Card.isValidNumber(cardNumber)
        val cardExpIsValid = Card.isValidDate(binding.editCardDate.text.toString())
        val cardCvvIsValid = Card.isValidCvv(binding.editCardCvv.text.toString())

        errorMode(!cardNumberIsValid, binding.editCardNumber)
        errorMode(!cardExpIsValid, binding.editCardDate)
        errorMode(!cardCvvIsValid, binding.editCardCvv)

        return cardNumberIsValid && cardExpIsValid && cardCvvIsValid
    }

    private fun openScanTypeDialog() {
        val itemsArray = arrayOf(
            getString(R.string.card_pay_fragment_camera_scan),
            getString(R.string.card_pay_fragment_nfc_scan)
        )
        AlertDialog.Builder(context).apply {
            setItems(itemsArray) { dialog, item ->
                when (item) {
                    ScannerType.CAMERA.ordinal -> getCardDataFromScanner.launch(ScannerType.CAMERA)
                    ScannerType.NFC.ordinal -> getCardDataFromScanner.launch(ScannerType.NFC)
                }
                dialog.dismiss()
            }
        }.show()
    }

    private fun showErrorScanningToast() {
        context?.let {
            Toast.makeText(it, R.string.card_pay_fragment_scan_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun isNfcEnable(): Boolean {
        return context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_NFC) == true
    }

    companion object {
        private const val CARD_NUMBER_MASK = "____ ____ ____ ____"
        private const val CARD_DATE_MASK = "__/__"
        private const val CARD_NUMBER_MAX_LENGTH = 19
        fun newInstance() = CardPayFragment()
    }
}
