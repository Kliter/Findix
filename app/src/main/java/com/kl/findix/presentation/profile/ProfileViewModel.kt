package com.kl.findix.presentation.profile

import android.graphics.Bitmap
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
    var _user: User = User()

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
        }
    }

    fun saveProfileSettings() {
        viewModelScope.launch {
            firebaseUser?.let {
                firebaseDataBaseService.updateProfileInfo(it, _user)
            }
        }
    }

    fun uploadProfileIcon(imageUrl: String) {
        viewModelScope.launch {
            firebaseUserService.getCurrentSignInUser()?.let {
                var bitmap: Bitmap? = null
                when (val result = imageService.getBitmap(imageUrl)) {
                    is ServiceResult.Success -> bitmap = result.data
                    is ServiceResult.Failure -> Log.e(TAG, result.error)
                }

                bitmap?.let { bitmap ->
                    firebaseStorageService.uploadProfileIcon(
                        it.uid,
                        imageService.getBytesFromBitmap(bitmap)
                    )
                }
            }
        }
    }
}
