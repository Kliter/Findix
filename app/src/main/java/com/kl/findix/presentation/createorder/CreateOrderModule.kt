package com.kl.findix.presentation.createorder

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class CreateOrderModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeCreateOrderFragment(): CreateOrderFragment

    @Binds
    @IntoMap
    @ViewModelKey(CreateOrderViewModel::class)
    abstract fun bindCreateOrderViewModel(viewModel: CreateOrderViewModel): ViewModel
}