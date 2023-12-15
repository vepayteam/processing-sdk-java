package io.finbridge.vepay.moneytransfersdk.presentation.fragments.threeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finbridge.vepay.moneytransfersdk.data.models.TransferStatus
import io.finbridge.vepay.moneytransfersdk.feature.sse.OkSse
import io.finbridge.vepay.moneytransfersdk.feature.sse.ServerSentEvent
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.Response

@HiltViewModel
class ThreeDSViewModel @Inject constructor() : ViewModel() {
    private val _paymentStatus =
        MutableStateFlow<TransferStatus>(TransferStatus.WAITING_CHECK_STATUS)
    val paymentStatus: StateFlow<TransferStatus> = _paymentStatus


    fun sseInitialization(invoiceUuid: String) {
        val request: Request = Request.Builder().url(SSE_TEST_URL).build()
        val okSse = OkSse()

        okSse.newServerSentEvent(request, object : ServerSentEvent.Listener {
            override fun onOpen(sse: ServerSentEvent?, response: Response?) {}

            override fun onMessage(
                sse: ServerSentEvent?,
                id: String?,
                event: String?,
                message: String?,
            ) {
                viewModelScope.launch {
                    if (id == invoiceUuid) {
                        event?.let {
                            _paymentStatus.emit(
                                when (it) {
                                    TransferStatus.WAITING.ordinal.toString() -> TransferStatus.WAITING
                                    TransferStatus.DONE.ordinal.toString() -> TransferStatus.DONE
                                    TransferStatus.ERROR.ordinal.toString() -> TransferStatus.ERROR
                                    TransferStatus.CANCEL.ordinal.toString() -> TransferStatus.CANCEL
                                    TransferStatus.NOT_EXEC.ordinal.toString() -> TransferStatus.NOT_EXEC
                                    TransferStatus.WAITING_CHECK_STATUS.ordinal.toString() -> TransferStatus.WAITING_CHECK_STATUS
                                    TransferStatus.REFUND_DONE.ordinal.toString() -> TransferStatus.REFUND_DONE
                                    else -> {
                                        TransferStatus.WAITING_CHECK_STATUS
                                    }
                                }
                            )
                        }
                    }
                }
            }

            override fun onComment(sse: ServerSentEvent?, comment: String?) {}

            override fun onRetryTime(sse: ServerSentEvent?, milliseconds: Long): Boolean {
                return false
            }

            override fun onRetryError(
                sse: ServerSentEvent?,
                throwable: Throwable?,
                response: Response?,
            ): Boolean {
                return true
            }

            override fun onClosed(sse: ServerSentEvent?) {}

            override fun onPreRetry(sse: ServerSentEvent?, originalRequest: Request?): Request? {
                return originalRequest
            }
        })
    }

    companion object {
        private const val SSE_TEST_URL =
            "https://develop-processing.backend.vepay.cf/h2hapi/v1/stream/sse?uuid=83b87bd5-1f51-4766-8aa5-0e08f9e1460a"
    }
}
