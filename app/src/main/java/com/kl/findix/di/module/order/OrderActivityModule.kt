package com.kl.findix.di.module.order

import com.kl.findix.presentation.order.CreateOrderFragment
import com.kl.findix.presentation.order.OrderDetailFragment
import com.kl.findix.presentation.order.OrderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OrderActivityModule {

    @ContributesAndroidInjector(modules = [OrderFragmentModule::class])
    internal abstract fun provideOrderFragment(): OrderFragment

    @ContributesAndroidInjector(modules = [CreateOrderFragmentModule::class])
    internal abstract fun provideCreateOrderFragment(): CreateOrderFragment

    @ContributesAndroidInjector(modules = [OrderDetailFragmentModule::class])
    internal abstract fun provideOrderDetailFragment(): OrderDetailFragment
}