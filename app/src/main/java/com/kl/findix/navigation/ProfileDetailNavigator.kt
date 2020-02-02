package com.kl.findix.navigation

import androidx.navigation.NavController

class ProfileDetailNavigator(
    private val navController: NavController
) {

    fun toPrev() {
        navController.navigateUp()
    }
}