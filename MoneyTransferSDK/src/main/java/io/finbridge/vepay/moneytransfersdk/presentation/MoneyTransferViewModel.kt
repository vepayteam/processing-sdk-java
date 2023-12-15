package io.finbridge.vepay.moneytransfersdk.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finbridge.vepay.moneytransfersdk.data.models.TransferStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MoneyTransferViewModel @Inject constructor() : ViewModel() {
    private val _paymentStatus =
        MutableStateFlow<TransferStatus>(TransferStatus.WAITING_CHECK_STATUS)
    val paymentStatus: StateFlow<TransferStatus> = _paymentStatus

    fun changePaymentStatus(transferStatus: TransferStatus) {
        viewModelScope.launch {
            _paymentStatus.emit(transferStatus)
        }
    }
}
