package com.kl.findix.presentation.order

import com.airbnb.epoxy.TypedEpoxyController
import com.kl.findix.model.Order
import com.kl.findix.presentation.order.view.orderListItemView

class OrderController(
    private val onClickOrder: (Order) -> Unit
) : TypedEpoxyController<List<OrderListItem>?>() {

    override fun buildModels(data: List<OrderListItem>?) {
        data?.forEach { orderListItem ->
            orderListItemView(orderListItem) {
                id("order_${orderListItem.order.orderId}")
                onClickOrder { order ->
                    onClickOrder.invoke(order)
                }
            }
        }
    }
}