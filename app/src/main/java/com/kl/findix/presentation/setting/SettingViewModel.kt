package com.kl.findix.presentation.setting

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.ServiceResult
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseUserService: FirebaseUserService,
    private val firebaseStorageService: FirebaseStorageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver,
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            firebaseUser?.let {
                when (val result = firebaseUserService.deleteAccount(it.uid)) {
                    is ServiceResult.Success -> {
                        firebaseDataBaseService.deleteUser(it.uid)
                        firebaseDataBaseService.deleteUserLocation(it.uid)
                        firebaseDataBaseService.deleteOrderFromUserId(it.uid)
                        firebaseStorageService.deleteUser(it.uid)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }
    }
}