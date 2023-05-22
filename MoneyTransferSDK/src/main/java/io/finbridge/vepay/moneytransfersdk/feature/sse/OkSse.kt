package io.finbridge.vepay.moneytransfersdk.feature.sse

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request

class OkSse @JvmOverloads constructor(
    client: OkHttpClient = OkHttpClient
        .Builder()
        .readTimeout(0, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
) {
    private val client: OkHttpClient

    init {
        this.client = client.newBuilder().protocols(listOf(Protocol.HTTP_1_1)).build()
    }

    fun newServerSentEvent(
        request: Request,
        listener: ServerSentEvent.Listener,
    ): ServerSentEvent {
        val sse = RealServerSentEvent(request, listener)
        sse.connect(client)
        return sse
    }
}
