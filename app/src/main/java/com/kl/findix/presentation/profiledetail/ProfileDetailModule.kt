package com.kl.findix.presentation.profiledetail

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class ProfileDetailModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProfileDetailFragment(): ProfileDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileDetailViewModel::class)
    abstract fun bindProfileDetailViewModel(viewModel: ProfileDetailViewModel): ViewModel
}