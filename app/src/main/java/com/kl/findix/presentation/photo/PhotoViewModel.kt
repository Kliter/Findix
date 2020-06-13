package com.kl.findix.presentation.photo

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhotoViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseStorageService: FirebaseStorageService
) : ViewModel(), LifecycleObserver {

    // Event
    val showWorkPhotoCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun showWorkPhoto(index: Int) {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                showWorkPhotoCommand.postValue(
                    firebaseStorageService.getWorkPhotoRef(firebaseUser.uid, index)
                )
            }
        }
    }
}