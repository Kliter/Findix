package com.kl.findix.presentation.profiledetail

import com.airbnb.epoxy.TypedEpoxyController
import com.kl.findix.itemProfileOrderList
import com.kl.findix.itemProfileOredersSectionHeader
import com.kl.findix.model.Order
import com.kl.findix.util.extension.getDateTimeText

class ProfileDetailController : TypedEpoxyController<List<Order>?>() {

    override fun buildModels(data: List<Order>?) {
        data ?: return

        if (data.isNotEmpty()) {
            itemProfileOredersSectionHeader {
                id(modelCountBuiltSoFar)
            }
        }

        data.forEach { order ->
            itemProfileOrderList {
                id("order_${order.orderId}")
                order(order)
                isShownMenu(false)
                order.timeStamp?.let {
                    dateTime(getDateTimeText(it))
                }
            }
        }
    }
}