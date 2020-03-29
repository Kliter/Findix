package com.kl.findix.di.module.order

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.ProfileDetailNavigator
import com.kl.findix.presentation.profiledetail.ProfileDetailFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ProfileDetailFragmentModule {

    @Binds
    abstract fun bindsFragment(fragment: ProfileDetailFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: ProfileDetailFragment): ProfileDetailNavigator {
            return ProfileDetailNavigator(fragment.findNavController())
        }
    }
}