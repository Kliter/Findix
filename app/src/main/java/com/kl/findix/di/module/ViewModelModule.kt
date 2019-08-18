package com.kl.findix.di.module

import androidx.lifecycle.ViewModel
import com.kl.findix.viewmodel.LoginViewModel
import com.kl.findix.viewmodel.MapsViewModel
import com.kl.findix.viewmodel.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun provideLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    internal abstract fun provideMapViewModel(mapsViewModel: MapsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun provideProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

}