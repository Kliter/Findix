package com.kl.findix.presentation.order

import com.airbnb.epoxy.Typed2EpoxyController
import com.kl.findix.itemProgressbar
import com.kl.findix.model.Order
import com.kl.findix.presentation.order.view.orderListItemView

class OrderController(
    private val onClickOrder: (Order) -> Unit,
    private val onLoadMore: () -> Unit
) : Typed2EpoxyController<Boolean, List<OrderListItem>?>() {

    override fun buildModels(hasMoreToLoad: Boolean, data: List<OrderListItem>?) {
        data?.forEach { orderListItem ->
            orderListItemView(orderListItem) {
                id("order_${orderListItem.order.orderId}")
                onClickOrder { order ->
                    onClickOrder.invoke(order)
                }
            }
        }

        if (hasMoreToLoad) {
            itemProgressbar {
                id(modelCountBuiltSoFar)
                spanSizeOverride { _, _, _ -> 2 }
                onBind { _, _, _ ->
                    onLoadMore.invoke()
                }
            }
        }
    }
}