package com.kl.findix.di.module.order

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.OrderNavigator
import com.kl.findix.presentation.order.OrderFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class OrderFragmentModule {

    @Binds
    abstract fun bindsFragment(fragment: OrderFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: OrderFragment): OrderNavigator {
            return OrderNavigator(fragment.findNavController())
        }
    }
}