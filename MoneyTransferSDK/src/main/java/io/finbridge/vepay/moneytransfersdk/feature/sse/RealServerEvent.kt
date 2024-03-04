package io.finbridge.vepay.moneytransfersdk.feature.sse

import io.finbridge.vepay.moneytransfersdk.core.utils.emptySymbol
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.BufferedSource

internal class RealServerSentEvent(request: Request, listener: ServerSentEvent.Listener) :
    ServerSentEvent {
    private val listener: ServerSentEvent.Listener
    private val originalRequest: Request
    private var client: OkHttpClient? = null
    private var call: Call? = null
    private var sseReader: Reader? = null
    private var reconnectTime = TimeUnit.SECONDS.toMillis(3)
    private var readTimeoutMillis: Long = 0
    private var lastEventId: String? = null

    init {
        require(GET_REQUEST == request.method) { request.method }
        originalRequest = request
        this.listener = listener
    }

    fun connect(client: OkHttpClient?) {
        this.client = client
        prepareCall(originalRequest)
        enqueue()
    }

    private fun prepareCall(request: Request) {
        if (client == null) {
            throw NullPointerException()
        }
        val requestBuilder: Request.Builder = request.newBuilder()
        call = client?.newCall(requestBuilder.build())
    }

    private fun enqueue() {
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                notifyFailure(e, null)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    openSse(response)
                } else {
                    notifyFailure(IOException(response.message), response)
                }
            }
        })
    }

    internal fun openSse(response: Response) {
        sseReader = Reader(response.body?.source())
        sseReader?.setTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS)
        listener.onOpen(this, response)
        while (call != null && call?.isCanceled() == false) {
            sseReader?.read()
        }
    }

    internal fun notifyFailure(throwable: Throwable, response: Response?) {
        if (!retry(throwable, response)) {
            listener.onClosed(this)
            close()
        }
    }

    private fun retry(throwable: Throwable, response: Response?): Boolean {
        if (!Thread.currentThread().isInterrupted
            && call?.isCanceled() == false
            && listener.onRetryError(this, throwable, response)
        ) {
            val request = listener.onPreRetry(this, originalRequest) ?: return false
            prepareCall(request)
            try {
                Thread.sleep(reconnectTime)
            } catch (ignored: InterruptedException) {
                return false
            }
            if (!Thread.currentThread().isInterrupted && call?.isCanceled() == false) {
                enqueue()
                return true
            }
        }
        return false
    }

    override fun request(): Request {
        return originalRequest
    }

    override fun setTimeout(timeout: Long, unit: TimeUnit?) {
        if (sseReader != null) {
            sseReader?.setTimeout(timeout, unit)
        }
        readTimeoutMillis = unit?.toMillis(timeout) ?: 0
    }

    override fun close() {
        if (call != null && call?.isCanceled() == false) {
            call?.cancel()
        }
    }

    private inner class Reader constructor(private val source: BufferedSource?) {
        private val data = StringBuilder()
        private var eventName = Companion.DEFAULT_EVENT

        fun read(): Boolean {
            try {
                val line = source?.readUtf8LineStrict()
                processLine(line)
            } catch (e: IOException) {
                notifyFailure(e, null)
                return false
            }
            return true
        }

        fun setTimeout(timeout: Long, unit: TimeUnit?) {
            unit?.let { source?.timeout()?.timeout(timeout, it) }
        }

        private fun processLine(line: String?) {
            if (line.isNullOrEmpty()) {
                dispatchEvent()
                return
            }
            val colonIndex = line.indexOf(Companion.COLON_DIVIDER)
            if (colonIndex == 0) {
                listener.onComment(
                    this@RealServerSentEvent,
                    line.substring(1).trim { it <= emptySymbol() })
            } else if (colonIndex != -1) {
                val field = line.substring(0, colonIndex)
                var value = Companion.EMPTY_STRING
                var valueIndex = colonIndex + 1
                if (valueIndex < line.length) {
                    if (line[valueIndex] == emptySymbol()) {
                        valueIndex++
                    }
                    value = line.substring(valueIndex)
                }
                processField(field, value)
            } else {
                processField(line, Companion.EMPTY_STRING)
            }
        }

        private fun dispatchEvent() {
            if (data.isEmpty()) return
            var dataString = data.toString()
            if (dataString.endsWith(WORD_WRAP)) {
                dataString = dataString.substring(0, dataString.length - 1)
            }
            listener.onMessage(this@RealServerSentEvent, lastEventId, eventName, dataString)
            data.setLength(0)
            eventName = Companion.DEFAULT_EVENT
        }

        private fun processField(field: String, value: String) {
            when {
                Companion.DATA == field -> {
                    data.append(value).append(WORD_WRAP)
                }

                Companion.ID == field -> {
                    lastEventId = value
                }

                Companion.EVENT == field -> {
                    eventName = value
                }

                Companion.RETRY == field && DIGITS_ONLY.matcher(value).matches() -> {
                    val timeout = value.toLong()
                    if (listener.onRetryTime(this@RealServerSentEvent, timeout)) {
                        reconnectTime = timeout
                    }
                }
            }
        }
    }

    companion object {
        private val DIGITS_ONLY = Pattern.compile("^[\\d]+$")
        private const val COLON_DIVIDER = ':'
        private const val DATA = "data"
        private const val ID = "id"
        private const val EVENT = "event"
        private const val RETRY = "retry"
        private const val DEFAULT_EVENT = "message"
        private const val EMPTY_STRING = ""
        private const val GET_REQUEST = "GET"
        private const val WORD_WRAP = "\n"
    }
}
