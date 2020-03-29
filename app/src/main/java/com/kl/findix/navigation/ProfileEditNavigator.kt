package com.kl.findix.navigation

import androidx.navigation.NavController
import com.kl.findix.presentation.profileedit.ProfileEditFragmentDirections

class ProfileEditNavigator(
    private val navController: NavController
) {

    fun toPrev() {
        navController.popBackStack()
    }

    fun toLogin() {
        navController.navigate(
            ProfileEditFragmentDirections.toLogin()
        )
    }
}