package com.kl.findix.firestore

import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kl.findix.model.User

interface UserService {
    fun signOut()
    fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount)
    fun getUserLiveData(): LiveData<User>
}