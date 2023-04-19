package io.finbridge.vepay.moneytransfersdk.api

interface KtorApiRequest<in R, out T> {
    suspend fun execute(request: R): T
}
