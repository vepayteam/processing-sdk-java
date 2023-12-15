package io.finbridge.vepay.moneytransfersdk.core.utils.extentions

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

fun <T : Parcelable> Intent.getSafetyParcelableExtra(name: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(name, clazz)
    } else this.getParcelableExtra(name)
}

inline fun <reified T : Serializable> Intent.getSafetySerializableExtra(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}
