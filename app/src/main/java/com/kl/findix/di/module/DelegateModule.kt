package com.kl.findix.di.module

import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.kl.findix.util.delegate.UiStateViewModelDelegateImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DelegateModule {

    @Singleton
    @Provides
    fun provideUiStateViewModelDelegate(): UiStateViewModelDelegate {
        return UiStateViewModelDelegateImpl()
    }
}