package com.kl.findix.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.firestore.UserService
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userService: UserService
): ViewModel() {

    private var userLiveData = userService.getUserLiveData()

    fun getUserLiveData() = userLiveData

    fun getCurrentSignInUser(): FirebaseUser? {
        return userService.getCurrentSignInUser()
    }

    fun signOut() {
        userService.signOut()
    }

    fun signIn(googleSignInAccount: GoogleSignInAccount?) {
        googleSignInAccount?.let {
            userService.signInWithGoogle(it)
        }
    }
}