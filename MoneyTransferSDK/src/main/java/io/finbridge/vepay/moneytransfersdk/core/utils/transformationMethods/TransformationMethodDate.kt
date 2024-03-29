package io.finbridge.vepay.moneytransfersdk.core.utils.transformationMethods

import android.text.method.PasswordTransformationMethod
import android.view.View

open class TransformationMethodDate : PasswordTransformationMethod() {

    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {

        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char {
            return  if (index == 2) '/' else  '●'
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex)
        }
    }
}