package com.kl.findix.di.module.login

import com.kl.findix.presentation.login.LoginFragment
import com.kl.findix.presentation.login.SignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginActivityModule {

    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    internal abstract fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    internal abstract fun provideSignUpFragment(): SignUpFragment

}