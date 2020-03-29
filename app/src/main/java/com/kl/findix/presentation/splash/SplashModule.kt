package com.kl.findix.presentation.splash

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class SplashModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
}