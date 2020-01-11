package com.kl.findix.navigation

import androidx.navigation.NavController

class OrderDetailNavigator(
    private val navController: NavController
) {
    fun toPrev() {
        navController.navigateUp()
    }
}