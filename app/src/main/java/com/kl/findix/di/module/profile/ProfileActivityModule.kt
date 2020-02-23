package com.kl.findix.di.module.profile

import com.kl.findix.presentation.profile.ProfileEditFragment
import com.kl.findix.presentation.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileActivityModule {

    @ContributesAndroidInjector(modules = [ProfileEditFragmentModule::class])
    internal abstract fun provideProfileEditFragment(): ProfileEditFragment

    @ContributesAndroidInjector(modules = [ProfileFragmentModule::class])
    internal abstract fun provideProfileFragment(): ProfileFragment

}