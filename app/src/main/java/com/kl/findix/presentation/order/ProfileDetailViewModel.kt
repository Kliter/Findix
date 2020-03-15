package com.kl.findix.presentation.order

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileDetailViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService
) : ViewModel(), LifecycleObserver {

    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user

    val setProfilePhotoCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo(userId: String) {
        viewModelScope.launch {
            firebaseDataBaseService.fetchUserInfo(userId) {
                _user.postValue(it)
                it.profilePhotoUrl?.isNotEmpty()?.let {
                    setProfilePhoto()
                }
            }
        }
    }

    private fun setProfilePhoto() {
        firebaseUser?.let { firebaseUser ->
            setProfilePhotoCommand.postValue(
                firebaseStorageService.getProfileIconRef(firebaseUser.uid)
            )
        }
    }
}