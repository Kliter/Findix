package com.kl.findix.di.module.order

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.CreateOrderNavigator
import com.kl.findix.presentation.order.CreateOrderFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class CreateOrderFrgamentModule {

    @Binds
    abstract fun bindsFragment(fragment: CreateOrderFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: CreateOrderFragment): CreateOrderNavigator {
            return CreateOrderNavigator(fragment.findNavController())
        }
    }
}