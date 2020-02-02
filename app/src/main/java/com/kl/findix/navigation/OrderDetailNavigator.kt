package com.kl.findix.navigation

import androidx.navigation.NavController
import com.kl.findix.presentation.order.OrderDetailFragmentDirections

class OrderDetailNavigator(
    private val navController: NavController
) {
    fun toPrev() {
        navController.navigateUp()
    }

    fun toCreateProfileDetailFragment(userId: String) {
        navController.navigate(
            OrderDetailFragmentDirections.actionOrderDetailToProfileDetail(userId)
        )
    }
}