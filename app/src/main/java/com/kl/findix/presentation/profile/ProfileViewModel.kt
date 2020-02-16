package com.kl.findix.presentation.profile

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
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
import com.kl.findix.util.safeLet
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseUserService: FirebaseUserService,
    private val firebaseStorageService: FirebaseStorageService,
    private val imageService: ImageService
) : ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    // State
    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user

    var profileIconBitmap: MutableLiveData<Bitmap> = MutableLiveData()
    var setProfileIconCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()

    private var _profilePhotoUri: Uri? = null

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo() {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                firebaseDataBaseService.fetchOwnProfileInfo(
                    firebaseUser = firebaseUser,
                    fetchOwnProfileInfoListener = { user ->
                        _user.postValue(user)
                    }
                )
            }

            // ProfilePhoto 取得してデフォルトで表示したいけどだるいからここまで
            firebaseUser?.let { firebaseUser ->

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
                        Log.e(TAG, result.error)
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
                Log.e(TAG, result.error)
            }
        }
    }

    fun setProfileIcon() {
        firebaseUser?.let { firebaseUser ->
            setProfileIconCommand.postValue(firebaseStorageService.getProfileIconRef(firebaseUser.uid))
        }
    }
}
