package com.kl.findix.navigation

import androidx.navigation.NavController

class ProfileEditNavigator (
    private val navController: NavController
) {

    fun toPrev() {
        navController.popBackStack()
    }
}