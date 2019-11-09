package com.kl.findix.di.module.maps

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.MapsNavigator
import com.kl.findix.presentation.map.MapsFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class MapsFragmentModule {

    @Binds
    abstract fun bindsFragment(fragment: MapsFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: MapsFragment): MapsNavigator {
            return MapsNavigator(fragment.findNavController())
        }
    }
}