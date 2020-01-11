package com.kl.findix.navigation

import androidx.navigation.NavController
import com.kl.findix.presentation.order.OrderFragmentDirections

class OrderNavigator (
    private val navController: NavController
) {
    fun toCreateOrderFragment() {
        navController.navigate(
            OrderFragmentDirections.actionOrderToCreateOrder()
        )
    }

    fun toOrderDetailFragment(orderId: String) {
        navController.navigate(
            OrderFragmentDirections.actionOrderToOrderDetail(orderId)
        )
    }

    fun toSearchFragment() {
//        navController.navigate(
//        )
    }
}