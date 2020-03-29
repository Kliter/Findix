package com.kl.findix.presentation.profileedit

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
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
import com.kl.findix.services.ImageService
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.kl.findix.util.extension.safeLet
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseUserService: FirebaseUserService,
    private val firebaseStorageService: FirebaseStorageService,
    private val imageService: ImageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver,
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    companion object {
        private const val TAG = "ProfileEditViewModel"
    }

    // State
    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user
    var profileIconBitmap: MutableLiveData<Bitmap> = MutableLiveData()


    // Event
    var setProfileIconCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()
    var hideRefreshCommand: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()

    private var _profilePhotoUri: Uri? = null

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo() {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                uiState.postValue(UiState.Loading)
                when (val result =
                    firebaseDataBaseService.fetchOwnProfileInfo(firebaseUser = firebaseUser)) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                        _user.postValue(result.data)
                        hideRefreshCommand.postValue(true)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }
    }

    fun saveProfile(contentResolver: ContentResolver) {
        safeLet(user.value, firebaseUser) { user, firebaseUser ->
            viewModelScope.launch {
                when (val result = firebaseDataBaseService.updateProfileInfo(
                    firebaseUser,
                    user,
                    _profilePhotoUri.toString()
                )) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }

        safeLet(
            firebaseUserService.getCurrentSignInUser(),
            _profilePhotoUri
        ) { currentSignInUser, profilePhotoUri ->
            viewModelScope.launch {
                when (val result = imageService.getBitmap(profilePhotoUri, contentResolver)) {
                    is ServiceResult.Success -> {
                        firebaseStorageService.uploadProfileIcon(
                            currentSignInUser.uid,
                            imageService.getBytesFromBitmap(result.data)
                        )
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }
    }

    fun updateProfilePhoto(uri: Uri, contentResolver: ContentResolver) {
        when (val result = imageService.getBitmap(uri, contentResolver)) {
            is ServiceResult.Success -> {
                _profilePhotoUri = uri
                profileIconBitmap.postValue(result.data)
            }
            is ServiceResult.Failure -> {
                // TODO: Error handling.
            }
        }
    }

    fun setProfileIcon() {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                setProfileIconCommand.postValue(
                    firebaseStorageService.getProfileIconRef(
                        firebaseUser.uid
                    )
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }
}