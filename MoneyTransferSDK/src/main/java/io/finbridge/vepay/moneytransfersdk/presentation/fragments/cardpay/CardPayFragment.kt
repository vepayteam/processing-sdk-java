package io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.data.models.card.Card
import io.finbridge.vepay.moneytransfersdk.data.models.card.CardType
import io.finbridge.vepay.moneytransfersdk.databinding.FragmentCardPayBinding
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay.scanner.CardScannerActivityContract
import ru.tinkoff.decoro.MaskDescriptor
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.DescriptorFormatWatcher

class CardPayFragment : Fragment() {

    private lateinit var binding: FragmentCardPayBinding
    private val viewModel: CardPayViewModel by viewModels()
    private val getCardDataFromScanner =
        registerForActivityResult(CardScannerActivityContract()) { data: Intent? ->
            validateDataFromCardScanner(data)
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

        binding.editCardNumber.doAfterTextChanged { editableText ->
            val cardNumber = editableText.toString()
            if (Card.isValidNumber(cardNumber)) {
                errorMode(false, binding.editCardNumber)
            } else {
                errorMode(cardNumber.length == CARD_NUMBER_MAX_LENGTH, binding.editCardNumber)
            }

            updatePaymentSystemIcon(cardNumber)
        }

        binding.editCardNumber.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidNumber(binding.editCardNumber.text.toString()),
                binding.editCardNumber
            )
        }

        binding.editCardDate.doAfterTextChanged { editableText ->
                val cardExp = editableText.toString()
                if (Card.isValidDate(cardExp)) {
                    binding.editCardCvv.requestFocus()
                    errorMode(false, binding.editCardDate)
                }
            }

        binding.editCardDate.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidDate(binding.editCardDate.text.toString()),
                binding.editCardDate
            )
        }

        binding.editCardCvv.doAfterTextChanged { editableText ->
                errorMode(false, binding.editCardCvv)

                if (Card.isValidCvv(editableText.toString())) {
                    val inputMethodManager =
                        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    binding.editCardCvv.clearFocus()
                }
            }

        binding.editCardCvv.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidCvv(binding.editCardCvv.text.toString()),
                binding.editCardCvv
            )
        }

        binding.buttonPay.setOnClickListener {
            if (isValid()) {
                //TODO платёж
            }
        }

        binding.btnScan.setOnClickListener {
            getCardDataFromScanner.launch(Unit)
        }
    }

    private fun validateDataFromCardScanner(data: Intent?) {
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            val scanResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data.getParcelableExtra<CreditCard>(
                    CardIOActivity.EXTRA_SCAN_RESULT, CreditCard::class.java
                )
            } else {
                data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
            }
            binding.editCardNumber.text?.append(scanResult?.cardNumber)
            if (scanResult?.isExpiryValid == true) {
                val month =
                    if (scanResult.expiryMonth in CARD_ONE_SIGN_MIN_EXPIRY_MONTH..CARD_ONE_SIGN_MAX_EXPIRY_MONTH) {
                        "$CARD_ONE_SIGN_ZERO${scanResult.expiryMonth}"
                    } else {
                        "${scanResult.expiryMonth}"
                    }
                val year = if (scanResult.expiryYear > TWENTY_CENTURY) {
                    "${scanResult.expiryYear - TWENTY_CENTURY}"
                } else {
                    "${scanResult.expiryYear}"
                }
                binding.editCardDate.text?.append("$month$year")
            }
        }
    }

    private fun errorMode(isErrorMode: Boolean, editText: TextInputEditText) {
        if (isErrorMode) {
            editText.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_red))
            editText.setBackgroundResource(R.drawable.edit_text_underline_error)
        } else {
            editText.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_blue_grey))
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

    companion object {
        private const val CARD_NUMBER_MASK = "____ ____ ____ ____"
        private const val CARD_DATE_MASK = "__/__"
        private const val CARD_NUMBER_MAX_LENGTH = 19
        private const val CARD_ONE_SIGN_MIN_EXPIRY_MONTH = 1
        private const val CARD_ONE_SIGN_MAX_EXPIRY_MONTH = 9
        private const val CARD_ONE_SIGN_ZERO = 0
        private const val TWENTY_CENTURY = 2000
        fun newInstance() = CardPayFragment()
    }
}
