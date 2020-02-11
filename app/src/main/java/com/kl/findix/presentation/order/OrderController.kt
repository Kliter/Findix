package com.kl.findix.presentation.order

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.kl.findix.itemOrderList
import com.kl.findix.model.Order
import com.kl.findix.util.getDateTimeText

class OrderController(
    private val onClickOrder: (Order) -> Unit
) : TypedEpoxyController<List<Order>?>() {

    override fun buildModels(data: List<Order>?) {
        data?.let { orders ->
            orders.forEach { order ->
                itemOrderList {
                    id("order_${order.orderId}")
                    order(order)
                    order.timeStamp?.let {
                        dateTime(getDateTimeText(it))
                    }
                    onClickOrder(View.OnClickListener {
                        onClickOrder(order)
                    })
                }
            }
        }
    }
}