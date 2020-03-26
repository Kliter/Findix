package com.kl.findix.di.module

import com.kl.findix.di.module.login.LoginFragmentModule
import com.kl.findix.di.module.login.SignUpFragmentModule
import com.kl.findix.di.module.maps.MapsFragmentModule
import com.kl.findix.di.module.order.CreateOrderFragmentModule
import com.kl.findix.di.module.order.OrderDetailFragmentModule
import com.kl.findix.di.module.order.OrderFragmentModule
import com.kl.findix.di.module.order.ProfileDetailFragmentModule
import com.kl.findix.di.module.profile.ProfileBottomSheetDialogModule
import com.kl.findix.di.module.profile.ProfileEditFragmentModule
import com.kl.findix.di.module.profile.ProfileFragmentModule
import com.kl.findix.presentation.login.LoginFragment
import com.kl.findix.presentation.login.SignUpFragment
import com.kl.findix.presentation.map.MapsFragment
import com.kl.findix.presentation.order.CreateOrderFragment
import com.kl.findix.presentation.order.OrderDetailFragment
import com.kl.findix.presentation.order.OrderFragment
import com.kl.findix.presentation.order.ProfileDetailFragment
import com.kl.findix.presentation.profile.ProfileBottomSheetDialog
import com.kl.findix.presentation.profile.ProfileEditFragment
import com.kl.findix.presentation.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    internal abstract fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [SignUpFragmentModule::class])
    internal abstract fun provideSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector(modules = [MapsFragmentModule::class])
    internal abstract fun provideMapsFragment(): MapsFragment

    @ContributesAndroidInjector(modules = [OrderFragmentModule::class])
    internal abstract fun provideOrderFragment(): OrderFragment

    @ContributesAndroidInjector(modules = [CreateOrderFragmentModule::class])
    internal abstract fun provideCreateOrderFragment(): CreateOrderFragment

    @ContributesAndroidInjector(modules = [OrderDetailFragmentModule::class])
    internal abstract fun provideOrderDetailFragment(): OrderDetailFragment

    @ContributesAndroidInjector(modules = [ProfileDetailFragmentModule::class])
    internal abstract fun provideProfileDetailFragment(): ProfileDetailFragment

    @ContributesAndroidInjector(modules = [ProfileEditFragmentModule::class])
    internal abstract fun provideProfileEditFragment(): ProfileEditFragment

    @ContributesAndroidInjector(modules = [ProfileFragmentModule::class])
    internal abstract fun provideProfileFragment(): ProfileFragment

    @ContributesAndroidInjector(modules = [ProfileBottomSheetDialogModule::class])
    internal abstract fun provideProfileBottomSheetDialog(): ProfileBottomSheetDialog
}