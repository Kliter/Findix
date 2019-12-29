package com.kl.findix.di.module

import androidx.lifecycle.ViewModel
import com.kl.findix.presentation.login.LoginViewModel
import com.kl.findix.presentation.login.SignUpViewModel
import com.kl.findix.presentation.map.MapsViewModel
import com.kl.findix.presentation.order.OrderViewModel
import com.kl.findix.presentation.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun provideLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(SignUpViewModel::class)
    internal abstract fun provideSignUpViewModel(signUpViewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(MapsViewModel::class)
    internal abstract fun provideMapViewModel(mapsViewModel: MapsViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun provideProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(OrderViewModel::class)
    internal abstract fun provideOrderViewModel(orderViewModel: OrderViewModel): ViewModel
}