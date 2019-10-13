package com.kl.findix.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.services.FirebaseUserServiceImpl
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserServiceImpl
): ViewModel() {

    val isSignedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun getCurrentSignInUser(): FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun signOut() {
        firebaseUserService.signOut()
    }

    fun signIn(googleSignInAccount: GoogleSignInAccount?) {
        googleSignInAccount?.let {
            firebaseUserService.signInWithGoogle(it)
        }
    }

    fun isSignedIn() {
        isSignedIn.postValue(firebaseUserService.getCurrentSignInUser() != null)
    }
}