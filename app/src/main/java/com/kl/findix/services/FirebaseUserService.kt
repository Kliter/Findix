package com.kl.findix.services

import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User

interface FirebaseUserService {
    fun getCurrentSignInUser(): FirebaseUser?
    suspend fun signOut()
    suspend fun signInWithGoogle(
        googleSignInAccount: GoogleSignInAccount
    ): ServiceResult<Unit>
    suspend fun getUserLiveData(): LiveData<User>
    suspend fun signUpWithEmail(email: String, password: String): ServiceResult<Unit>
    suspend fun signInWithEmail(email: String, password: String): ServiceResult<Unit>
}