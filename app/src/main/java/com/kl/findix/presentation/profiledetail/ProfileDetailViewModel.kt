package com.kl.findix.presentation.profiledetail

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileDetailViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver, UiStateViewModelDelegate by uiStateViewModelDelegate {

    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user

    val setProfilePhotoCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo(userId: String) {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            when (val result = firebaseDataBaseService.fetchUserInfo(userId)) {
                is ServiceResult.Success -> {
                    uiState.postValue(UiState.Loaded)
                    result.data?.let { user ->
                        _user.postValue(user)
                        user.profilePhotoUrl?.isNotEmpty()?.let {
                            setProfilePhoto()
                        }
                    }
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
            }
        }
    }

    private fun setProfilePhoto() {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                setProfilePhotoCommand.postValue(
                    firebaseStorageService.getProfilePhotoRef(firebaseUser.uid)
                )
            }
        }
    }
}