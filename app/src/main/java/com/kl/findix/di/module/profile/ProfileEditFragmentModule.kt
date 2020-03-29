package com.kl.findix.di.module.profile

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.ProfileEditNavigator
import com.kl.findix.presentation.profileedit.ProfileEditFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ProfileEditFragmentModule {

    @Binds
    abstract fun bindsFragment(fragment: ProfileEditFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: ProfileEditFragment): ProfileEditNavigator {
            return ProfileEditNavigator(fragment.findNavController())
        }
    }
}