package com.kl.findix.services

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.ServiceResult

interface FirebaseUserService {
    fun getCurrentSignInUser(): FirebaseUser?
    suspend fun signOut()
    suspend fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount): ServiceResult<Unit>
    suspend fun signUpWithEmail(email: String, password: String): ServiceResult<Unit>
    suspend fun signInWithEmail(email: String, password: String): ServiceResult<Unit>
    suspend fun deleteAccount(userId: String): ServiceResult<Unit>
}