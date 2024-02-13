package io.finbridge.vepay.moneytransfersdk.presentation.fragments.yourCard

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.core.utils.extentions.ResourceProvider
import io.finbridge.vepay.moneytransfersdk.data.models.network.AcsRedirect
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.Card
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.CardUi
import io.finbridge.vepay.moneytransfersdk.data.usecase.InvoicePaymentUseCase
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class YourCardViewModel @Inject constructor(
    private val invoicePaymentUseCase: InvoicePaymentUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val _cardModel: MutableStateFlow<List<CardUi>> = MutableStateFlow(
        listOf(
            CardUi(
                id = ID_ADD,
                iconRes = R.drawable.ic_add,
                bankName = "Новая карта",
                isActive = false
            )
        )
    )

    val cardModel = _cardModel.asStateFlow()

    private val _acsRedirect: MutableStateFlow<AcsRedirect?> = MutableStateFlow(null)
    val acsRedirect = _acsRedirect.asStateFlow()

    private val _payError: MutableSharedFlow<Int> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val payError = _payError.asSharedFlow()

    fun activationCard(item: CardUi) {
        viewModelScope.launch {
            var stationItems = mutableListOf<CardUi>()
            _cardModel.value.forEach {
                stationItems.add(it.copy(isActive = false))
            }
            var updatedItem = stationItems.find { it.id == item.id }
            val index = stationItems.indexOf(updatedItem)
            if (updatedItem != null) {
                updatedItem = updatedItem.copy(isActive = true)
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            _cardModel.emit(stationItems)
        }
    }

    fun editCardNumber(cardNumber: String) {
        viewModelScope.launch {
            var stationItems = mutableListOf<CardUi>()
            _cardModel.value.forEach {
                stationItems.add(it)
            }
            var updatedItem = stationItems.find { it.isActive }
            val index = stationItems.indexOf(updatedItem)
            if (updatedItem != null) {

                updatedItem =
                    updatedItem.copy(card = updatedItem.card.copy(cardNumber = cardNumber))
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            if (Card.isValidNumber(cardNumber)) _cardModel.emit(stationItems)
        }
    }

    fun editDate(date: String) {
        viewModelScope.launch {
            var stationItems = mutableListOf<CardUi>()
            _cardModel.value.forEach {
                stationItems.add(it)
            }
            var updatedItem = stationItems.find { it.isActive }
            val index = stationItems.indexOf(updatedItem)
            if (updatedItem != null) {
                updatedItem = updatedItem.copy(card = updatedItem.card.copy(expireDate = date))
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            if (Card.isValidDate(date)) _cardModel.emit(stationItems)
        }
    }

    fun editCVV(cvv: String) {
        viewModelScope.launch {
            var stationItems = mutableListOf<CardUi>()
            _cardModel.value.forEach {
                stationItems.add(it)
            }
            var updatedItem = stationItems.find { it.isActive }
            val index = stationItems.indexOf(updatedItem)
            if (updatedItem != null) {
                updatedItem = updatedItem.copy(card = updatedItem.card.copy(cvv = cvv))
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            if (Card.isValidCvv(cvv)) _cardModel.emit(stationItems)
        }
    }

    fun editIcon(icon: Int) {
        viewModelScope.launch {
            var stationItems = mutableListOf<CardUi>()
            _cardModel.value.forEach {
                stationItems.add(it)
            }
            var updatedItem = stationItems.find { it.isActive }
            val index = stationItems.indexOf(updatedItem)
            if (updatedItem != null) {
                updatedItem = updatedItem.copy(iconRes = icon)
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            if (updatedItem?.id != ID_ADD) {
                _cardModel.emit(stationItems)
            } else {
                //TODO ADD CardModel LOGICAL
            }
        }
    }

    fun editBankName(bankName: String) {
        viewModelScope.launch {
            var stationItems = mutableListOf<CardUi>()
            _cardModel.value.forEach {
                stationItems.add(it)
            }
            var updatedItem = stationItems.find { it.isActive }
            val index = stationItems.indexOf(updatedItem)
            if (updatedItem != null) {
                updatedItem = updatedItem.copy(bankName = bankName)
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            if (updatedItem?.id != ID_ADD) {
                _cardModel.emit(stationItems)
            } else {
                //TODO ADD CardModel LOGICAL
            }
        }
    }

    fun editColor(colorRes: Int) {
        viewModelScope.launch {
            var stationItems = mutableListOf<CardUi>()
            _cardModel.value.forEach {
                stationItems.add(it)
            }
            var updatedItem = stationItems.find { it.isActive }
            val index = stationItems.indexOf(updatedItem)
            if (updatedItem != null) {
                updatedItem = updatedItem.copy(colorRectangleRes = colorRes)
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            _cardModel.emit(stationItems)
        }
    }

    fun findCard(action: (card: CardUi) -> Unit) {
        viewModelScope.launch {
            val stationItems = mutableListOf<CardUi>()
            _cardModel.value.forEach {
                stationItems.add(it)
            }
            val updatedItem = stationItems.find { it.isActive }

            if (updatedItem != null) {
                action.invoke(updatedItem)
            }
        }
    }

    fun pay(
        id: String,
        xUser: String,
        screenHeight: Int,
        screenWidth: Int,
    ) {
        viewModelScope.launch {
            invoicePaymentUseCase.pay(
                id = id,
                xUser = xUser,
                card = cardModel.value.first().card,
                screenHeight = screenHeight,
                screenWidth = screenWidth
            ).fold(
                ifLeft = {
                    _payError.emit(it.getErrorMessageResource())
                    Toast.makeText(
                        resourceProvider.getContext(),
                        it.getErrorMessageResource(),
                        Toast.LENGTH_LONG
                    ).show()
                },
                ifRight = {
                    it.acsRedirect.let { acsRedirect ->
                        _acsRedirect.emit(acsRedirect ?: AcsRedirect())
                    }
                }
            )
        }
    }

    companion object {
        const val ID_ADD: Int = 9999
    }
}
