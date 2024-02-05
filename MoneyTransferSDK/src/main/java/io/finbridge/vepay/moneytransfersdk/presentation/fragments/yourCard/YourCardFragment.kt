package io.finbridge.vepay.moneytransfersdk.presentation.fragments.yourCard

import android.content.Context
import android.graphics.Insets
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.Card
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.CardBank
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.CardType
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.CardUi
import io.finbridge.vepay.moneytransfersdk.databinding.FragmentYourCardBinding
import io.finbridge.vepay.moneytransfersdk.presentation.adapter.CardAdapter
import io.finbridge.vepay.moneytransfersdk.presentation.fragments.threeds.ThreeDSFragment
import kotlinx.coroutines.launch
import ru.tinkoff.decoro.MaskDescriptor
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.DescriptorFormatWatcher


@AndroidEntryPoint
class YourCardFragment : Fragment() {
    private lateinit var binding: FragmentYourCardBinding
    private val viewModel: YourCardViewModel by viewModels()
    private val invoiceUuid: String
        get() = requireArguments().getString(UUID_KEY) ?: emptyString()
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
    private val cardAdapter = CardAdapter(
        controllerColor = { rectangle, colorRes ->
            rectangle.setColorFilter(
                requireContext().getColor(colorRes),
                PorterDuff.Mode.SRC_IN
            )
            binding.bankInfo.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    colorRes
                )
            )

        },
        onClick = { _, _ -> }
    )
    private var isEventAutoActivation: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentYourCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            cardNumberFormatWatcher.installOn(editCardNumber)
            cardDateFormatWatcher.installOn(editCardDate)
            rcCard.adapter = cardAdapter
        }
        initDoAfterTextChanged()
        initOnFocusChangeListeners()
        initCard()
        rsScrollListener()
        lifecycleListener()
        initClickListeners()
    }

    private fun initClickListeners() {
        with(binding) {
            btTransferPay.setOnClickListener {
                with(binding) {
                    parentLayout.isVisible = false
                    progressBar.isVisible = true
                }
                viewModel.pay(
                    id = invoiceUuid,
                    screenHeight = getScreenHeight(),
                    screenWidth = getScreenWidth(),
                )
            }
        }
    }

    private fun lifecycleListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cardModel.collect {
                cardAdapter.submitList(it)
                if (it.isEmpty()) {
                    binding.rcCard.isVisible = false
                }
            }
        }
        lifecycleScope.launch {
            viewModel.acsRedirect.collect {
                if (it != null) {
                    parentFragmentManager.beginTransaction()
                        .add(
                            R.id.fragment_container,
                            ThreeDSFragment.newInstance(
                                acsUrl = it.url ?: emptyString(),
                                md = it.postParameters?.md ?: emptyString(),
                                paReq = it.postParameters?.paReq ?: emptyString(),
                                term = it.postParameters?.termUrl ?: emptyString(),
                                invoiceUuid = invoiceUuid
                            )
                        )
                        .commit()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.payError.collect {
                if (it != -1) {
                    with(binding) {
                        parentLayout.isVisible = true
                        progressBar.isVisible = false
                    }
                }
            }
        }
    }

    private fun rsScrollListener() {
        with(binding) {
            rcCard.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = rcCard.layoutManager as LinearLayoutManager
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                    val globalVisibleRect = Rect()
                    rcCard.getGlobalVisibleRect(globalVisibleRect)
                    for (pos in firstPosition..lastPosition) {
                        val view = layoutManager.findViewByPosition(pos)
                        if (view != null) {
                            val percentage = getPercentage(view)
                            if (percentage == 100.0 && isEventAutoActivation) {
                                val currentItem = cardAdapter.currentList[pos]
                                viewModel.activationCard(currentItem)
                                updateCardInfo(currentItem)
                                break
                            } else continue

                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState in 0..1) {
                        isEventAutoActivation = true
                    }
                }
            })
        }
    }

    private fun errorMode(
        isErrorMode: Boolean,
        isEditCardNumber: Boolean = false,
        action: () -> Unit = {}
    ) {
        if (isErrorMode && isEditCardNumber) {
            binding.cardError.isVisible = true
        } else {
            action.invoke()
        }
    }

    private fun updatePaymentSystemInfo(cardNumber: String) {
        val cardType = CardType.getType(cardNumber)
        val bankInfo = CardBank.getType(cardNumber)
        val logo = bankInfo.getLogoRes()
        val icon = bankInfo.getIconRes()
        val psIcon = cardType.getCardInfoIconRes()
        val bankName = requireContext().getString(bankInfo.getNameRes())

        viewModel.editBankName(bankName)
        updateColor(bankInfo)
        with(binding) {
            imgBankLogo.setImageResource(logo)
            if (cardNumber.isNotEmpty() && icon != null) {
                viewModel.editIcon(icon)
            }
            if (cardNumber.isEmpty() || psIcon == null) {
                icPs.isVisible = false
            } else {
                bankNameLogo.isVisible = true
                icPs.isVisible = true
                icPs.setImageResource(psIcon)
            }
        }
    }

    private fun initOnFocusChangeListeners() {
        with(binding) {
            editCardCvv.setOnFocusChangeListener { _, hasFocus ->
                errorMode(
                    !hasFocus && !Card.isValidCvv(editCardCvv.text.toString())
                )
            }
            editCardDate.setOnFocusChangeListener { _, hasFocus ->
                errorMode(
                    !hasFocus && !Card.isValidDate(editCardDate.text.toString())
                )
            }
            editCardNumber.setOnFocusChangeListener { _, hasFocus ->
                errorMode(
                    !hasFocus && !Card.isValidNumber(editCardNumber.text.toString()), true
                )
                if (hasFocus || Card.isValidNumber(editCardNumber.text.toString()) || editCardNumber.text.toString()
                        .isEmpty()
                ) {
                    binding.cardError.isVisible = false
                }
            }
            editCardCvv.setOnEditorActionListener { _, actionId, event ->
                if (event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(editCardCvv.windowToken, 0)
                }
                false
            }
        }
    }

    private fun initDoAfterTextChanged() {
        with(binding) {
            editCardNumber.doAfterTextChanged { editableText ->
                activationButton()
                val cardNumber = editableText.toString()
                if (Card.isValidNumber(cardNumber) || editCardNumber.hasFocus() || editableText?.isEmpty() == true) {
                    errorMode(false)
                    binding.cardError.isVisible = false
                    if (cardNumber.length == CARD_NUMBER_MAX_LENGTH)
                        viewModel.editCardNumber(cardNumber)
                } else {
                    errorMode(
                        Card.isValidNumber(cardNumber),
                    )
                }
                updatePaymentSystemInfo(cardNumber)
            }

            editCardDate.doAfterTextChanged { editableText ->
                activationButton()
                val cardExp = editableText.toString()
                if (Card.isValidDate(cardExp) || editCardDate.hasFocus() || editableText?.isEmpty() == true) {
                    errorMode(false) {
                        viewModel.findCard { card ->
                            if (Card.isValidNumber(editCardNumber.text.toString()) && Card.isValidCvv(
                                    binding.editCardCvv.text.toString()
                                )
                            ) {
                                binding.cardError.isVisible = false
                                correctCardState(card)
                            }
                        }
                    }
                    viewModel.editDate(cardExp)
                } else {
                    errorMode(true)
                }
            }

            editCardCvv.doAfterTextChanged { editableText ->
                activationButton()
                val cvv = editableText.toString()
                if (Card.isValidCvv(cvv) || editCardCvv.hasFocus() || editableText?.isEmpty() == true) {
                    errorMode(false) {
                        viewModel.findCard { card ->
                            if (Card.isValidNumber(editCardNumber.text.toString())
                                && Card.isValidDate(editCardDate.text.toString())
                            ) {
                                binding.cardError.isVisible = false
                                correctCardState(card)
                            }
                        }
                    }
                    if (cvv.length == CVV_MAX_LENGTH) {
                        viewModel.editCVV(cvv)
                    }
                } else {
                    errorMode(true)
                }
            }
        }
    }

    private fun activationButton() {
        with(binding) {
            btTransferPay.isEnabled =
                Card.isValidNumber(editCardNumber.text.toString())
                        && Card.isValidDate(editCardDate.text.toString())
                        && Card.isValidCvv(editCardCvv.text.toString())
        }
    }

    private fun correctCardState(card: CardUi) {
        context?.let {
            val bankInfo = CardBank.getType(card.card.cardNumber)
            val logo = bankInfo.getLogoRes()
            binding.imgBankLogo.setImageResource(logo)
            updateColor(bankInfo)
        }
    }

    private fun initCard() {
        val initCard = viewModel.cardModel.value.first()
        updateCardInfo(initCard)
    }

    private fun updateCardInfo(card: CardUi) {
        with(binding) {
            editCardCvv.setText(card.card.cvv)
            editCardDate.setText(card.card.expireDate)
            editCardNumber.setText(card.card.cardNumber)
        }
        updateColor(CardBank.getType(card.card.cardNumber))
    }

    private fun updateColor(bankInfo: CardBank) {
        when (bankInfo) {
            CardBank.UNKNOWN -> {
                viewModel.editColor(R.color.strawberry)
            }

            CardBank.ALFA_BANK -> {
                viewModel.editColor(R.color.alfa_card)
            }

            CardBank.SBER_BANK -> {
                viewModel.editColor(R.color.sbrebank_card)
            }

            CardBank.TINKOFF_BANK -> {
                viewModel.editColor(R.color.tinkoff_card)
            }

            CardBank.RAIFFEISEN_BANK -> {
                viewModel.editColor(R.color.raifasenk_card)
            }

            else -> {
                viewModel.editColor(R.color.strawberry)
            }
        }
    }

    private fun getPercentage(view: View): Double {
        val itemRect = Rect()
        val isParentViewEmpty = view.getLocalVisibleRect(itemRect)
        val visibleWidth = itemRect.width().toDouble()
        val width = view.measuredWidth
        val viewVisibleHeightPercentage = visibleWidth / width * 100

        return if (isParentViewEmpty) {
            viewVisibleHeightPercentage
        } else {
            0.0
        }
    }

    private fun getScreenWidth(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val insets: Insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            requireActivity().resources.displayMetrics.widthPixels
        }
    }

    private fun getScreenHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val insets: Insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.left - insets.right
        } else {
            requireActivity().resources.displayMetrics.heightPixels
        }
    }

    companion object {
        private const val UUID_KEY = "uuid_invoice"
        private const val CARD_NUMBER_MASK = "____ ____ ____ ____"
        private const val CARD_DATE_MASK = "__/__"
        private const val CARD_NUMBER_MAX_LENGTH = 19
        private const val CVV_MAX_LENGTH = 3

        @JvmStatic
        fun newInstance(invoiceUuid: String) = YourCardFragment().apply {
            arguments = bundleOf(
                UUID_KEY to invoiceUuid
            )
        }
    }
}
