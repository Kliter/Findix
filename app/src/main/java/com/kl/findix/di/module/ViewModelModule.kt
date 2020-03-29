package com.kl.findix.di.module

import androidx.lifecycle.ViewModelProvider
import com.kl.findix.di.FindixViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Module used to define the connection between the framework's [ViewModelProvider.Factory] and
 * our own implementation: [IOSchedViewModelFactory].
 */
@Module
@Suppress("UNUSED")
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: FindixViewModelFactory):
            ViewModelProvider.Factory
}