package com.kl.findix.navigation

import androidx.navigation.NavController
import com.kl.findix.presentation.signup.SignUpFragmentDirections

class SignUpNavigator(
    private val navController: NavController
) {

    fun toPrev() {
        navController.popBackStack()
    }

    fun toMaps() {
        navController.navigate(
            SignUpFragmentDirections.toMaps()
        )
    }
}