package com.kl.findix.di.module.order

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kl.findix.navigation.OrderDetailNavigator
import com.kl.findix.presentation.order.OrderDetailFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class OrderDetailFragmentModule {

    @Binds
    abstract fun bindsFragment(fragment: OrderDetailFragment): Fragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesNavController(fragment: OrderDetailFragment): OrderDetailNavigator {
            return OrderDetailNavigator(fragment.findNavController())
        }
    }
}