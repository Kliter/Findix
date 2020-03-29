package com.kl.findix.presentation.profile

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class ProfileModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProfileBottomSheetDialog(): ProfileBottomSheetDialog

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel
}