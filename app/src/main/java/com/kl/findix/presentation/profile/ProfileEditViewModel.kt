package com.kl.findix.presentation.profile

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
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
import com.kl.findix.util.FindixError
import com.kl.findix.util.FindixError.NetworkError
import com.kl.findix.util.UiState
import com.kl.findix.util.safeLet
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseUserService: FirebaseUserService,
    private val firebaseStorageService: FirebaseStorageService,
    private val imageService: ImageService
) : ViewModel(), LifecycleObserver {

    companion object {
        private const val TAG = "ProfileEditViewModel"
    }

    // State
    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    val uiState: LiveData<UiState>
        get() = _uiState
    var profileIconBitmap: MutableLiveData<Bitmap> = MutableLiveData()


    // Event
    val showErrorDialogCommand: PublishLiveDataKtx<String> = PublishLiveDataKtx()
    var setProfileIconCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()
    var hideRefreshCommand: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()

    private var _profilePhotoUri: Uri? = null

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo() {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                _uiState.postValue(UiState.Loading)
                when (val result =
                    firebaseDataBaseService.fetchOwnProfileInfo(firebaseUser = firebaseUser)) {
                    is ServiceResult.Success -> {
                        _user.postValue(result.data)
                        hideRefreshCommand.postValue(true)
                    }
                    is ServiceResult.Failure -> {
                        when (val exception = result.exception) {
                            is NetworkError -> {
                                _uiState.postValue(UiState.Retry)
                            }
                            is FindixError.UndefinedError -> {
                                _uiState.postValue(UiState.Error)
                                showErrorDialogCommand.postValue(exception.alertMessage)
                            }
                            else -> {
                                _uiState.postValue(UiState.Error)
                                exception.message?.let { message ->
                                    showErrorDialogCommand.postValue(message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun saveProfile(contentResolver: ContentResolver) {
        user.value?.let { user ->
            viewModelScope.launch {
                firebaseUser?.let {
                    firebaseDataBaseService.updateProfileInfo(
                        it,
                        user,
                        _profilePhotoUri.toString()
                    )
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
                        // TODO: Error handling.
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
            setProfileIconCommand.postValue(firebaseStorageService.getProfileIconRef(firebaseUser.uid))
        }
    }

    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }
}