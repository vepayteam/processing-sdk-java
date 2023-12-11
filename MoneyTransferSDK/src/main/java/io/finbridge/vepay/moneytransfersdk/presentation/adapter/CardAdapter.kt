package io.finbridge.vepay.moneytransfersdk.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import io.finbridge.vepay.moneytransfersdk.data.models.ui.card.CardUi
import io.finbridge.vepay.moneytransfersdk.databinding.ItemCardActiveBinding
import io.finbridge.vepay.moneytransfersdk.databinding.ItemCardBinding
import io.finbridge.vepay.moneytransfersdk.presentation.base.BaseAdapter
import io.finbridge.vepay.moneytransfersdk.presentation.base.BaseViewHolder

class CardAdapter(
    private val onClick: (data: CardUi, position: Int) -> Unit,
    private val controllerColor: (rectangle: ImageView, color: Int) -> Unit,
) : BaseAdapter<ViewBinding, CardUi, BaseViewHolder<CardUi, ViewBinding>>() {

    inner class CardHolder(private val binding: ViewBinding) :
        BaseViewHolder<CardUi, ViewBinding>(binding) {

        override fun bind(item: CardUi, context: Context) {

            with(binding) {

                when (this) {
                    is ItemCardBinding -> {
                        tvTitle.text = item.name
                        tvLabel.text = item.bankName
                        imgLogo.setImageResource(item.iconRes)
                        cardView.setOnClickListener {
                            onClick.invoke(item, adapterPosition)
                        }

                    }

                    is ItemCardActiveBinding -> {
                        tvTitle.text = item.name
                        tvLabel.text = item.bankName
                        imgLogo.setImageResource(item.iconRes)
                        item.colorRectangleRes?.let { controllerColor(imgRectangle, it) }
                        cardView.setOnClickListener {
                            onClick.invoke(item, adapterPosition)
                        }
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<CardUi, ViewBinding> {
        val binding = when (viewType) {
            CARD_ACTIVE -> ItemCardActiveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            CARD_PASSIVE -> ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            else -> {
                throw RuntimeException("Unknown view type: $viewType")
            }
        }
        return CardHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isActive) {
            CARD_ACTIVE
        } else {
            CARD_PASSIVE
        }
    }

    companion object {
        const val CARD_PASSIVE = 200
        const val CARD_ACTIVE = 201
    }
}
