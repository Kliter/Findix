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

    fun toSearchFragment() {
//        navController.navigate(
//        )
    }
}