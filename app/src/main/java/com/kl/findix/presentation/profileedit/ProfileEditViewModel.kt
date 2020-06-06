package com.kl.findix.presentation.profileedit

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.model.Photo
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.services.ImageService
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.kl.findix.util.extension.MutableListLiveData
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
        private const val WORK_PHOTO_LIMIT = 5
    }

    // State
    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user

    private val _photos: MutableListLiveData<Photo> = MutableListLiveData()
    val photos: MutableList<Photo>
        get() = _photos

    private val _photoReferences: MutableListLiveData<StorageReference?> =
        MutableListLiveData<StorageReference?>(
            (0..WORK_PHOTO_LIMIT).map { null }.toMutableList()
        )
    val photoReferences: MutableListLiveData<StorageReference?>
        get() = _photoReferences

    // Event
    var hideRefreshCommand: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()

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

    fun fetchWorkPhotos() {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                uiState.postValue(UiState.Loading)
                (0..4).forEach {
                    val result = firebaseStorageService.getWorkPhotoRef(firebaseUser.uid, it)
                    _photoReferences[it] = result
                }
            }
        }
    }

    fun saveProfile() {
        safeLet(user.value, firebaseUser) { user, firebaseUser ->
            viewModelScope.launch {
                when (val result = firebaseDataBaseService.updateProfileInfo(firebaseUser, user)) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }
    }

    fun savePhoto() {
        viewModelScope.launch {
            photos.forEachIndexed { index, photo ->
                photo.bitmap?.let { bitmap ->
                    val photoByteArray =
                        (imageService.getBytesFromBitmap(bitmap) as? ServiceResult.Success)?.data
                    safeLet(firebaseUser?.uid, photoByteArray) { id, photoByteArray ->
                        uploadWorkPhoto(id, photoByteArray, index + 1)
                    }
                }
            }
        }
    }

    private fun uploadWorkPhoto(id: String, photoByteArray: ByteArray, number: Int) {
        viewModelScope.launch {
            when (val result = firebaseStorageService.uploadWorkPhoto(
                id,
                photoByteArray,
                number
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

    fun updateWorkPhoto(number: Int, uri: Uri, contentResolver: ContentResolver) {
        val result = imageService.getBitmap(uri, contentResolver)
        photos[number - 1] = Photo(
            (result as? ServiceResult.Success)?.data,
            uri
        )
    }

    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }
}