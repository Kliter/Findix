package com.kl.findix.di.module.login

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.SignUpNavigator
import com.kl.findix.presentation.login.SignUpFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class SignUpFragmentModule {

    @Binds
    abstract fun bindsFragment(fragment: SignUpFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: SignUpFragment): SignUpNavigator {
            return SignUpNavigator(fragment.findNavController())
        }
    }

}