package com.kl.findix.di.module.order

import com.kl.findix.presentation.order.OrderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OrderActivityModule {

    @ContributesAndroidInjector(modules = [OrderFragmentModule::class])
    internal abstract fun provideOrderFragment(): OrderFragment

}