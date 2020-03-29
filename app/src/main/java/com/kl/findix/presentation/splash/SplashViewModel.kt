package com.kl.findix.presentation.splash

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl.findix.services.FirebaseUserService
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService
): ViewModel(), LifecycleObserver {

    val isAlreadySignedIn: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()

    fun isAlreadySignedIn() {
        viewModelScope.launch {
            isAlreadySignedIn.postValue(firebaseUserService.getCurrentSignInUser() != null)
        }
    }
}