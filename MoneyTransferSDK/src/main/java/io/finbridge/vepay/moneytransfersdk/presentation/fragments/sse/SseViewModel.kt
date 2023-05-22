package io.finbridge.vepay.moneytransfersdk.presentation.fragments.sse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString
import io.finbridge.vepay.moneytransfersdk.feature.sse.OkSse
import io.finbridge.vepay.moneytransfersdk.feature.sse.ServerSentEvent
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Request
import okhttp3.Response

@HiltViewModel
class SseViewModel @Inject constructor() : ViewModel() {
    private val _sseMessage = MutableStateFlow<String>(emptyString())
    val sseMessage: StateFlow<String> = _sseMessage

    init {
        sseInitialization()
    }

    private fun sseInitialization() {
        val request: Request = Request.Builder().url(TEST_URL).build()
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
                    message?.let { _sseMessage.emit(it) }
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
        private const val TEST_URL =
            "https://feat-vpbc-1931-processing.backend.vepay.cf/h2hapi/v1/stream/sse?uuid=fffec8fa-ca7f-4e74-81c7-26fc8095bd2d"
    }
}

