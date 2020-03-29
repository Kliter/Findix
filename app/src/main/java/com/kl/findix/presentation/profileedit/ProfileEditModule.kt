package com.kl.findix.presentation.profileedit

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class ProfileEditModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProfileEditFragment(): ProfileEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileEditViewModel::class)
    abstract fun bindProfileEditViewModel(viewModel: ProfileEditViewModel): ViewModel
}