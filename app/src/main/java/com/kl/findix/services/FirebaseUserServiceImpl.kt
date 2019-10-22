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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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

    override suspend fun getCurrentSignInUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    override suspend fun signOut() {
        mAuth.signOut()
    }

    override suspend fun signInWithGoogle(
        googleSignInAccount: GoogleSignInAccount,
        googleSignInSuccessListener: () -> Unit,
        googleSignInFailedListener: () -> Unit
    ) {
        coroutineScope {
            launch {
                try {
                    val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
                    mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Google signin is succeed.")
                            val user = mAuth.currentUser
                            user?.let {
                                launch {
                                    signUpGoogleAccount(user)
                                }
                            }
                            googleSignInSuccessListener.invoke()
                        }
                        else {
                            Log.d(TAG, "Google signin is failed.")
                            googleSignInFailedListener.invoke()
                        }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                    googleSignInFailedListener.invoke()
                }
            }
        }
    }

    override suspend fun getUserLiveData(): MutableLiveData<User> = mUserData

    private suspend fun signUpGoogleAccount(firebaseUser: FirebaseUser) {
        coroutineScope {
            launch {
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
        }
    }

    private suspend fun isAlreadySignUp(): Boolean {
        coroutineScope {
            launch {
                val usersReference = firebaseFirestore.collection("Users").document(mAuth.currentUser?.uid!!)
                usersReference.get().addOnCompleteListener { document ->
                    Log.d(TAG, "exist")
                }
            }
        }
        // Todo: Implementation.
        return true
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        emailSignUpSuccessListener: () -> Unit,
        emailSignUpFailedListener: () -> Unit
    ) {
        coroutineScope {
            launch {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail: Succeed.")
                        emailSignUpSuccessListener.invoke()
                    } else {
                        Log.d(TAG, "createUserWithEmail: Failed.")
                        emailSignUpFailedListener.invoke()
                    }
                }
            }
        }
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String,
        emailSignInSuccessListener: () -> Unit,
        emailSignInFailedListener: () -> Unit
    ) {
        coroutineScope {
            launch {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail: Succeed.")
                        emailSignInSuccessListener.invoke()
                    } else {
                        Log.d(TAG, "signInWithEmail: Failed.")
                        emailSignInFailedListener.invoke()
                    }
                }
            }
        }
    }
}