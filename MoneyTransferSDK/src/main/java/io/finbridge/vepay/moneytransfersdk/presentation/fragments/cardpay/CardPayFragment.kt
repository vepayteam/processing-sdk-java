package io.finbridge.vepay.moneytransfersdk.presentation.fragments.cardpay

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.data.models.card.Card
import io.finbridge.vepay.moneytransfersdk.data.models.card.CardType
import io.finbridge.vepay.moneytransfersdk.databinding.FragmentCardPayBinding
import ru.tinkoff.decoro.MaskDescriptor
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.DescriptorFormatWatcher


class CardPayFragment : Fragment() {

    companion object {
        private const val CARD_NUMBER_MASK = "____ ____ ____ ____"
        private const val CARD_DATE_MASK = "__/__"
        private const val CARD_NUMBER_MAX_LENGTH = 19
        fun newInstance() = CardPayFragment()
    }

    private var _binding: FragmentCardPayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CardPayViewModel by viewModels()

    private val cardNumberFormatWatcher by lazy {
        val descriptor = MaskDescriptor.ofRawMask(CARD_NUMBER_MASK)
            .setTerminated(true)
            .setForbidInputWhenFilled(true)

        DescriptorFormatWatcher(UnderscoreDigitSlotsParser(), descriptor)
    }

    private val cardDateFormatWatcher by lazy {
        val descriptor = MaskDescriptor.ofRawMask(CARD_DATE_MASK)
            .setTerminated(true)
            .setForbidInputWhenFilled(true)

        DescriptorFormatWatcher(UnderscoreDigitSlotsParser(), descriptor)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCardPayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardNumberFormatWatcher.installOn(binding.editCardNumber)
        cardDateFormatWatcher.installOn(binding.editCardDate)

        binding.editCardNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editableText: Editable?) {
                val cardNumber = editableText.toString().replace(" ", "")
                if (Card.isValidNumber(cardNumber)) {
                    errorMode(false, binding.editCardNumber)
                } else {
                    errorMode(cardNumber.length == CARD_NUMBER_MAX_LENGTH, binding.editCardNumber)
                }

                updatePaymentSystemIcon(cardNumber)
            }
        })

        binding.editCardNumber.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidNumber(binding.editCardNumber.text.toString()),
                binding.editCardNumber
            )
        }

        binding.editCardDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editableText: Editable?) {

                val cardExp = editableText.toString()
                if (Card.isValidDate(cardExp)) {
                    binding.editCardCvv.requestFocus()
                    errorMode(false, binding.editCardDate)
                }
            }
        })

        binding.editCardDate.setOnFocusChangeListener { _, hasFocus ->
            errorMode(
                !hasFocus && !Card.isValidDate(binding.editCardDate.text.toString()),
                binding.editCardDate
            )
        }

        binding.editCardCvv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editableText: Editable?) {
                errorMode(false, binding.editCardCvv)

                if (Card.isValidCvv(editableText.toString())) {
                    val inputMethodManager = requireActivity()
                        .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    binding.editCardCvv.clearFocus()
                }
            }
        })

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
            //TODO сканирование карты
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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

}
