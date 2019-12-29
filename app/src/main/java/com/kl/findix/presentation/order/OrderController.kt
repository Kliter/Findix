package com.kl.findix.presentation.order

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.kl.findix.itemOrderList
import com.kl.findix.model.Order

class OrderController(
    private val onClickOrder: (Order) -> Unit
) : TypedEpoxyController<List<Order>?>() {

    override fun buildModels(data: List<Order>?) {
        data?.let { orders ->
            orders.forEach { order ->
                itemOrderList {
                    id(modelCountBuiltSoFar)
                    order(order)
                    onClickOrder(View.OnClickListener {
                        onClickOrder(order)
                    })
                }
            }
        }
    }
}