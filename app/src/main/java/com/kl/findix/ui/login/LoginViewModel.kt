package com.kl.findix.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseUserServiceImpl
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserServiceImpl
): ViewModel() {

    var user: MutableLiveData<User> = firebaseUserService.getUserLiveData()

    fun getCurrentSignInUser(): FirebaseUser? {
        return firebaseUserService.getCurrentSignInUser()
    }

    fun signOut() {
        firebaseUserService.signOut()
    }

    fun signIn(googleSignInAccount: GoogleSignInAccount?) {
        googleSignInAccount?.let {
            firebaseUserService.signInWithGoogle(it)
        }
    }
}