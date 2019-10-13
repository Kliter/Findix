package com.kl.findix.navigation

import androidx.navigation.NavController
import com.kl.findix.presentation.login.LoginFragmentDirections

class LoginNavigator (
    private val navController: NavController
) {

    fun toSignUpFragment() {
        navController.navigate(
            LoginFragmentDirections.actionLoginToSignUp()
        )
    }
}