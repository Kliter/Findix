package com.kl.findix.presentation.profile

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
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

    var user: User = User()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun saveProfileSettings() {
        viewModelScope.launch {
            firebaseUser?.let {
                firebaseDataBaseService.updateProfileInfo(it, user)
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
                    firebaseStorageService.uploadProfileIcon(it.uid, imageService.getBytesFromBitmap(bitmap))
                }
            }
        }
    }
}