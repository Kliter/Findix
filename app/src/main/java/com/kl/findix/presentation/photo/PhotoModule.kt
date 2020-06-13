package com.kl.findix.presentation.photo

import androidx.lifecycle.ViewModel
import com.kl.findix.di.FragmentScoped
import com.kl.findix.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
@Suppress("UNUSED")
abstract class PhotoModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): PhotoFragment

    @Binds
    @IntoMap
    @ViewModelKey(PhotoViewModel::class)
    abstract fun bindProfileViewModel(viewModel: PhotoViewModel): ViewModel
}