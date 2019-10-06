package com.kl.findix.di.module.maps

import com.kl.findix.ui.map.MapsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MapsActivityModule {

    @ContributesAndroidInjector(modules = [MapsFragmentModule::class])
    internal abstract fun provideMapsFragment(): MapsFragment

}