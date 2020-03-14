package com.kl.findix.navigation

import androidx.navigation.NavController
import com.kl.findix.presentation.profile.ProfileFragmentDirections

class ProfileNavigator (
    private val navController: NavController
) {

    fun toProfileEdit() {
        navController.navigate(
            ProfileFragmentDirections.toProfileEdit()
        )
    }
}