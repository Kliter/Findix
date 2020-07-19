package com.kl.findix.presentation.profile

import com.airbnb.epoxy.TypedEpoxyController
import com.kl.findix.itemProfileOrderList
import com.kl.findix.itemProfileOredersSectionHeader
import com.kl.findix.model.Order
import com.kl.findix.util.extension.getDateTimeText

class ProfileController(
    private val onClickMenu: (String) -> Unit
) : TypedEpoxyController<List<Order>?>() {

    override fun buildModels(data: List<Order>?) {
        itemProfileOredersSectionHeader {
            id("header")
        }
        data?.forEach { order ->
            itemProfileOrderList {
                id("order_${order.orderId}")
                order(order)
                isShownMenu(true)
                order.timeStamp?.let {
                    dateTime(getDateTimeText(it))
                }
                onClickMenu { _ ->
                    onClickMenu.invoke(order.orderId)
                }
            }
        }
    }
}