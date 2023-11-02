package io.finbridge.vepay.moneytransfersdk.presentation.fragments.yourCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.Card
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.CardUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YourCardViewModel @Inject constructor() : ViewModel() {
    private val tempCard = mutableSetOf(
        CardUi(
            id = ID_ADD,
            bankName = "Введите данные карты",
            iconRes = R.drawable.ic_add
        )
    )

    private val _cardModel: MutableStateFlow<List<CardUi>> = MutableStateFlow(
        listOf(
            CardUi(
                id = 0,
                card = Card("5486 7427 7411 8755", "08/25", "test1", "333"),
                name = "testName1",
                bankName = "Альфа-банк",
                iconRes = R.drawable.ic_alfa,
                isActive = true
            ),
            CardUi(
                id = 1,
                card = Card("4276 2600 2133 7950", "08/25", "test2", "333"),
                name = "testName2",
                bankName = "СберБанк",
                iconRes = R.drawable.ic_sber,
                isActive = false
            ),
            CardUi(
                id = 2,
                card = Card("4276 6000 2927 8518", "08/25", "test3", "333"),
                name = "testName3",
                bankName = "СберБанк",
                iconRes = R.drawable.ic_sber,
                isActive = false
            ),
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
            if (index == -1) {
                _cardModel.emit(tempCard.toList())
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
            if (index == -1) {
                _cardModel.emit(tempCard.toList())
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            if (updatedItem?.id != ID_ADD) {
                if (Card.isValidNumber(cardNumber)) _cardModel.emit(stationItems)
            } else {
                //TODO ADD CardModel LOGICAL
            }
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
            if (index == -1) {
                _cardModel.emit(tempCard.toList())
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            if (updatedItem?.id != ID_ADD) {
                if (Card.isValidDate(date)) _cardModel.emit(stationItems)
            } else {
                //TODO ADD CardModel LOGICAL
            }
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
            if (index == -1) {
                _cardModel.emit(tempCard.toList())
            }
            stationItems = stationItems.toMutableList().apply {
                if (updatedItem != null) {
                    this[index] = updatedItem
                }
            }
            if (updatedItem?.id != ID_ADD) {
                if (Card.isValidCvv(cvv)) _cardModel.emit(stationItems)
            } else {
                //TODO ADD CardModel LOGICAL
            }
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
            if (index == -1) {
                _cardModel.emit(tempCard.toList())
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
            if (index == -1) {
                _cardModel.emit(tempCard.toList())
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
            if (index == -1) {
                _cardModel.emit(tempCard.toList())
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