package com.kl.findix.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kl.findix.model.User
import javax.inject.Inject

class FirebaseUserService @Inject constructor(
    var mAuth: FirebaseAuth
): UserService {

    private var mUserData: MutableLiveData<User> = MutableLiveData()

    override fun getCurrentSignInUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    override fun signOut() {
        mAuth.signOut()
    }

    override fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount) {
        val authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        mAuth.signInWithCredential(authCredential)
    }

    override fun getUserLiveData(): LiveData<User> = mUserData

}