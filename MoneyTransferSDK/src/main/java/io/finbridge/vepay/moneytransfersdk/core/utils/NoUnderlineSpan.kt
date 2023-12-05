package io.finbridge.vepay.moneytransfersdk.core.utils

import android.text.TextPaint
import android.text.style.UnderlineSpan

class NoUnderlineSpan : UnderlineSpan() {

    override fun updateDrawState(ds: TextPaint) {
        ds.color = ds.linkColor
        ds.isUnderlineText = false
    }
}
