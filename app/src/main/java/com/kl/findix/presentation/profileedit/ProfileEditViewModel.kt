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
import com.kl.findix.util.extension.safeLet
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.delay
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

    val photos: MutableList<MutableLiveData<Photo>> = (0..4).map {
        MutableLiveData<Photo>()
    }.toMutableList()

    // Event
    var hideRefreshCommand: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()
    val setWorkPhotosCommand: MutableLiveData<Pair<Int, StorageReference>> = MutableLiveData()

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

    fun setWorkPhotos() {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                uiState.postValue(UiState.Loading)
                (0..4).forEach {
                    delay(100) // 待たずにpostすると5だけになる
                    val reference = firebaseStorageService.getWorkPhotoRef(firebaseUser.uid, it + 1)
                    setWorkPhotosCommand.postValue(Pair(it + 1, reference))
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
                photo.value?.bitmap?.let { bitmap ->
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
        photos[number - 1].postValue(
            Photo(
                (result as? ServiceResult.Success)?.data,
                uri
            )
        )
    }

    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }
}