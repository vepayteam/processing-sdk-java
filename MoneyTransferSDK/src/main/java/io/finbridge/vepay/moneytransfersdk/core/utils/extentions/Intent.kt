package io.finbridge.vepay.moneytransfersdk.core.utils.extentions

import android.content.Intent
import android.os.Build
import android.os.Parcelable

fun <T: Parcelable> Intent.getSafetyParcelableExtra(name: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(name, clazz)
    }
    else this.getParcelableExtra(name)
}
