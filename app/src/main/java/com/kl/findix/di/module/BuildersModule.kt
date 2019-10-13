package com.kl.findix.di.module

import com.kl.findix.di.module.login.LoginActivityModule
import com.kl.findix.di.module.maps.MapsActivityModule
import com.kl.findix.di.module.profile.ProfileActivityModule
import com.kl.findix.presentation.list.ListActivity
import com.kl.findix.presentation.login.LoginActivity
import com.kl.findix.presentation.map.MapsActivity
import com.kl.findix.presentation.message.MessageActivity
import com.kl.findix.presentation.profile.ProfileActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [MapsActivityModule::class])
    internal abstract fun provideMapActivity(): MapsActivity

    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    internal abstract fun provideLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [ProfileActivityModule::class])
    internal abstract fun provideProfileActivity(): ProfileActivity

    @ContributesAndroidInjector
    internal abstract fun provideMessageActivity(): MessageActivity

    @ContributesAndroidInjector
    internal abstract fun provideListActivity(): ListActivity

}