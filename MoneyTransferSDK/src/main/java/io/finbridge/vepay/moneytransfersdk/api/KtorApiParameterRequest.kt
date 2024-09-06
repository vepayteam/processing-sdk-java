package io.finbridge.vepay.moneytransfersdk.api

interface KtorApiParameterRequest<in R, in P, in S, in B, out T> {
    suspend fun execute(request: R, param1: P, param2: S, param3: B): T
}
