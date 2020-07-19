package com.kl.findix.presentation.setting

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class SettingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeSettingFragment(): SettingFragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindSettingViewModel(viewModel: SettingViewModel): ViewModel
}