package com.kl.findix.navigation

import androidx.navigation.NavController

class SignUpNavigator(
    private val navController: NavController
) {

    fun toPrev() {
        navController.popBackStack()
    }
}