package com.kl.findix.presentation.map

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class MapsModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeCreateMapsFragment(): MapsFragment

    @Binds
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    abstract fun bindCreateOrderViewModel(viewModel: MapsViewModel): ViewModel
}