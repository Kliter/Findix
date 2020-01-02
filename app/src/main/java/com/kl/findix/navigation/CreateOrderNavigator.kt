package com.kl.findix.navigation

import androidx.navigation.NavController

class CreateOrderNavigator(
    private val navController: NavController
) {
    fun toPrev() {
        navController.navigateUp()
    }
}