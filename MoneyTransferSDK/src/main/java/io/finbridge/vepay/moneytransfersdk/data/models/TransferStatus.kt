package io.finbridge.vepay.moneytransfersdk.data.models

enum class TransferStatus {
    WAITING,
    DONE,
    ERROR,
    CANCEL,
    NOT_EXEC,
    WAITING_CHECK_STATUS,
    REFUND_DONE,
}
