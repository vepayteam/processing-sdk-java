package io.finbridge.vepay.moneytransfersdk.core.utils.extentions

import io.finbridge.vepay.moneytransfersdk.data.repository.Response

suspend fun <T> getApiContentSafeOperation(operation: suspend () -> T): Response<T> {
    return try {
        Response.Success(operation.invoke())
    } catch (e: Exception) {
        Response.Failure(e)
    }
}
