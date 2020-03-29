package com.kl.findix.presentation.orderdetail

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class OrderDetailModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeOrderDetailFragment(): OrderDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailViewModel::class)
    abstract fun bindOrderDetailViewModel(viewModel: OrderDetailViewModel): ViewModel
}