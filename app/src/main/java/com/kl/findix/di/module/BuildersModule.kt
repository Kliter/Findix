package com.kl.findix.di.module

import com.kl.findix.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun provideMainActivity(): MainActivity

}