package com.kl.findix.navigation

import androidx.navigation.NavController
import com.kl.findix.presentation.map.MapsFragmentDirections

class MapsNavigator(
    private val navController: NavController
) {

    fun toLogin() {
        navController.navigate(
            MapsFragmentDirections.toLogin()
        )
    }
}