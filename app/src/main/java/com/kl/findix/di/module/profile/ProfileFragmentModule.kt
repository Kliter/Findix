package com.kl.findix.di.module.profile

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.ProfileNavigator
import com.kl.findix.presentation.profile.ProfileFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ProfileFragmentModule {

    @Binds
    abstract fun bindsFragment(fragment: ProfileFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: ProfileFragment): ProfileNavigator {
            return ProfileNavigator(fragment.findNavController())
        }
    }
}