package io.finbridge.vepay.moneytransfersdk.core.utils.transformationMethods

import android.text.method.PasswordTransformationMethod
import android.view.View
import io.finbridge.vepay.moneytransfersdk.core.utils.emptySymbol

open class TransformationMethodCardNumber : PasswordTransformationMethod() {

    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {

        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char {
            return if (index == 4 || index == 9 || index == 14) emptySymbol() else if (index !in 15..mSource.length) '•' else mSource[index]
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex)
        }
    }
}