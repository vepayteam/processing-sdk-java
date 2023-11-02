package io.finbridge.vepay.moneytransfersdk.data.models.ui.card

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.finbridge.vepay.moneytransfersdk.R
import io.finbridge.vepay.moneytransfersdk.core.utils.emptyString
import io.finbridge.vepay.moneytransfersdk.presentation.base.BaseAdapterTypes

data class CardUi(
    override val id: Int,
    val card: Card = Card(emptyString(), emptyString(), emptyString(), emptyString()),
    val name: String = "Другой банк",
    val bankName: String = emptyString(),
    @DrawableRes val iconRes: Int,
    @ColorRes val colorRectangleRes: Int? = null,
    val isActive: Boolean = false,
) : BaseAdapterTypes()
