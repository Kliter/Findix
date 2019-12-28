package com.kl.findix.presentation.profile

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.services.ImageService
import com.kl.findix.util.safeLet
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseUserService: FirebaseUserService,
    private val firebaseStorageService: FirebaseStorageService,
    private val imageService: ImageService
): ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    // State
    var user: MutableLiveData<User> = MutableLiveData()

    var profilePhotoBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    var _user: User = User()
    private var _profilePhotoUri: Uri? = null

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo() {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                firebaseDataBaseService.fetchProfileInfo(
                    firebaseUser = firebaseUser,
                    fetchProfileInfoListener = { user ->
                        this@ProfileViewModel._user = user
                        this@ProfileViewModel.user.postValue(user)
                    }
                )
            }

            // ProfilePhoto 取得してデフォルトで表示したいけどだるいからここまで
        }
    }

    fun saveProfile(contentResolver: ContentResolver) {
        safeLet(firebaseUserService.getCurrentSignInUser(), _profilePhotoUri) { currentSignInUser, profilePhotoUri ->
            viewModelScope.launch {
                firebaseUser?.let {
                    firebaseDataBaseService.updateProfileInfo(
                        it,
                        _user,
                        _profilePhotoUri.toString()
                    )
                }
            }
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
                profilePhotoBitmap.postValue(result.data)
            }
            is ServiceResult.Failure -> {
                Log.e(TAG, result.error)
            }
        }
    }
}
