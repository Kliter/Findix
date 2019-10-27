package com.kl.findix.presentation.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseUserService
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseUserService: FirebaseUserService
): ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    var user: User = User()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun saveProfileSettings() {
        firebaseUser?.let {
            firebaseDataBaseService.updateProfileInfo(it, user)
        }
    }
}