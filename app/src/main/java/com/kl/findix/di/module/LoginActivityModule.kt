package com.kl.findix.di.module

import com.kl.findix.ui.login.LoginFragment
import com.kl.findix.ui.login.SignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginActivityModule {

    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    internal abstract fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    internal abstract fun provideSignUpFragment(): SignUpFragment

}