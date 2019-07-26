package com.kl.findix.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kl.findix.model.User

class FirebaseUserService: UserService {

    private var mUserData: MutableLiveData<User> = MutableLiveData()
    private val mAuth = FirebaseAuth.getInstance()
    private val mAuthStateListener = FirebaseAuth.AuthStateListener {
        mAuth.currentUser?.let {
            mUserData.value = User(it.uid, it.displayName, it.photoUrl.toString(), it.email)
        }
    }

    init {
        mAuth.addAuthStateListener(mAuthStateListener)
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