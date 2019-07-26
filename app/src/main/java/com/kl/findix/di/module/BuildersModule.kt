package com.kl.findix.di.module

import com.kl.findix.ui.login.LoginActivity
import com.kl.findix.ui.map.MapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun provideMapActivity(): MapActivity

    @ContributesAndroidInjector
    internal abstract fun provideLoginActivity(): LoginActivity

}