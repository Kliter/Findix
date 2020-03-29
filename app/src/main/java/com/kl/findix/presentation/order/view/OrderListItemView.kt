package com.kl.findix.presentation.order.view

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.kl.findix.R
import com.kl.findix.databinding.ItemOrderListBinding
import com.kl.findix.model.Order
import com.kl.findix.presentation.order.OrderListItem
import com.kl.findix.util.extension.getDateTimeText

@EpoxyModelClass(layout = R.layout.item_order_list)
abstract class OrderListItemView(
    @EpoxyAttribute var orderListItem: OrderListItem
) : EpoxyModelWithHolder<OrderListItemView.ViewHolder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClickOrder: ((Order) -> Unit)? = null

    override fun bind(holder: ViewHolder) {
        holder.binding.order = orderListItem.order
        orderListItem.order.timeStamp?.let {
            holder.binding.dateTime =
                getDateTimeText(it)
        }
        holder.binding.setOnClickOrder {
            onClickOrder?.invoke(orderListItem.order)
        }
        Glide.with(holder.binding.image)
            .load(orderListItem.orderPhotoRef)
            .into(holder.binding.image)
    }

    class ViewHolder : EpoxyHolder() {
        lateinit var binding: ItemOrderListBinding
        override fun bindView(itemView: View) {
            binding = ItemOrderListBinding.bind(itemView)
        }
    }
}