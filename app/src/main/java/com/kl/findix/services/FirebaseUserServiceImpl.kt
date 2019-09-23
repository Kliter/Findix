package com.kl.findix.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kl.findix.R
import com.kl.findix.model.User
import javax.inject.Inject

class FirebaseUserServiceImpl @Inject constructor(
    val context: Context,
    var mAuth: FirebaseAuth
) : FirebaseUserService {

    companion object {
        private const val TAG = "FirebaseUserServiceImpl"
    }

    private var mUserData: MutableLiveData<User> = MutableLiveData()

    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun getCurrentSignInUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    override fun signOut() {
        mAuth.signOut()
    }

    override fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount) {
        try {
            val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
            mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {



                    val user = mAuth.currentUser
                    user?.let {
                        signUpGoogleAccount(user)
                    }
                }
                else {
                    Log.d(TAG, "Google signin is failed.")
                }
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    override fun getUserLiveData(): MutableLiveData<User> = mUserData

    private fun signUpGoogleAccount(firebaseUser: FirebaseUser) {
        val email = firebaseUser.email.toString()
        if (email.isNotEmpty()) {
            val user = User()
            user.email = email
            user.userName = email.let {
                it.substring(0, it.indexOf("@"))
            }
            user.userId = FirebaseAuth.getInstance().uid

            val newUserReference: DocumentReference = firebaseFirestore
                .collection(context.getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().uid!!)
            newUserReference.set(user)
        }
    }

    private fun isAlreadySignUp(): Boolean {
        val usersReference = firebaseFirestore.collection("Users").document(mAuth.currentUser?.uid!!)
        usersReference.get().addOnCompleteListener { document ->
            Log.d(TAG, "exist")
        }
        return true
    }
}