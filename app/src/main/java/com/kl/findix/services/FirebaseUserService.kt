package com.kl.findix.services

import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.User

interface FirebaseUserService {
    fun getCurrentSignInUser(): FirebaseUser?
    fun signOut()
    fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount)
    fun getUserLiveData(): LiveData<User>
    fun signUpGoogleAccount(firebaseUser: FirebaseUser)
}