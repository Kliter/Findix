package com.kl.findix.navigation

import androidx.navigation.NavController
import com.kl.findix.presentation.order.OrderDetailFragmentDirections

class OrderDetailNavigator(
    private val navController: NavController
) {
    fun toPrev() {
        navController.navigateUp()
    }

    fun toProfileDetailFragment(userId: String) {
        navController.navigate(
            OrderDetailFragmentDirections.toProfileDetail(userId)
        )
    }
}