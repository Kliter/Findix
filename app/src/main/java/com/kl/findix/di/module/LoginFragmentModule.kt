package com.kl.findix.di.module

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.LoginNavigator
import com.kl.findix.ui.login.LoginFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class LoginFragmentModule {

    @Binds
    abstract fun bindsFragment(fragment: LoginFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: LoginFragment): LoginNavigator {
            return LoginNavigator(fragment.findNavController())
        }
    }

}