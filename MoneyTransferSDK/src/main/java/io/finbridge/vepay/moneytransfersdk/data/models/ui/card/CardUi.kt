package io.finbridge.vepay.moneytransfersdk.data.models.ui.card

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.finbridge.vepay.moneytransfersdk.presentation.base.BaseAdapterTypes

data class CardUi(
    override val id:Int,
    val card: Card,
    val name: String,
    val bankName: String,
    @DrawableRes  val iconRes: Int,
    @ColorRes val colorRectangleRes:Int?=null,
    val isActive: Boolean = false,
): BaseAdapterTypes()
