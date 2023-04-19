package io.finbridge.vepay.moneytransfersdk.api

interface KtorApiParameterRequest<in R, in P, out T> {
    suspend fun execute(request: R, parameter: P): T
}
