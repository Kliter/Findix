package com.kl.findix.di.module

import androidx.lifecycle.ViewModel
import com.kl.findix.presentation.MainViewModel
import com.kl.findix.presentation.login.LoginViewModel
import com.kl.findix.presentation.login.SignUpViewModel
import com.kl.findix.presentation.map.MapsViewModel
import com.kl.findix.presentation.order.CreateOrderViewModel
import com.kl.findix.presentation.order.OrderDetailViewModel
import com.kl.findix.presentation.order.OrderViewModel
import com.kl.findix.presentation.order.ProfileDetailViewModel
import com.kl.findix.presentation.profile.ProfileEditViewModel
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
    @ViewModelKey(MainViewModel::class)
    internal abstract fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel

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

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(CreateOrderViewModel::class)
    internal abstract fun provideCreateOrderViewModel(createOrderViewModel: CreateOrderViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(OrderDetailViewModel::class)
    internal abstract fun provideOrderDetailViewModel(orderDetailViewModel: OrderDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(ProfileDetailViewModel::class)
    internal abstract fun provideProfileDetailViewModel(profileDetailViewModel: ProfileDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(ProfileEditViewModel::class)
    internal abstract fun provideProfileEditViewModel(profileEditViewModel: ProfileEditViewModel): ViewModel
}