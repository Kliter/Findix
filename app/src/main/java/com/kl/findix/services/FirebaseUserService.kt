package com.kl.findix.services

import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.SignInInfo
import com.kl.findix.model.User

interface FirebaseUserService {
    fun getCurrentSignInUser(): FirebaseUser?
    fun signOut()
    fun signInWithGoogle(
        googleSignInAccount: GoogleSignInAccount,
        googleSignInSuccessListener: () -> Unit,
        googleSignInFailedListener: () -> Unit
    )
    fun getUserLiveData(): LiveData<User>
    fun signUpWithEmail(
        signInInfo: SignInInfo,
        emailSignUpSuccessListener: () -> Unit,
        emailSignUpFailedListener: () -> Unit
    )
    fun signInWithEmail(
        signInInfo: SignInInfo,
        emailSignInSuccessListener: () -> Unit,
        emailSignInFailedListener: () -> Unit
    )
}