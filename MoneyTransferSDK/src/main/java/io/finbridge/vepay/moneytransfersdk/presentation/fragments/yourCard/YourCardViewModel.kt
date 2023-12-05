package io.finbridge.vepay.moneytransfersdk.presentation.fragments.yourCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.Card
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.CardUi
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class YourCardViewModel @Inject constructor() : ViewModel() {
    private val _cardModel: MutableStateFlow<List<CardUi>> = MutableStateFlow(
        listOf(
            CardUi(
                id = ID_ADD,
                iconRes = R.drawable.ic_add,
                bankName = "Введите данные карты",
                isActive = false
            )
        )
    )

    val cardModel = _cardModel.asStateFlow()

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

    companion object {
        const val ID_ADD: Int = 9999
    }
}
