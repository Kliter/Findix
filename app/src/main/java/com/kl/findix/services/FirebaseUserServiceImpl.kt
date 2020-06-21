package com.kl.findix.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kl.findix.R
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseUserServiceImpl @Inject constructor(
    val context: Context,
    var mAuth: FirebaseAuth,
    var firestore: FirebaseFirestore
) : FirebaseUserService {
    companion object {
        private const val TAG = "FirebaseUserServiceImpl"
    }

    override fun getCurrentSignInUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    override suspend fun signOut() {
        mAuth.signOut()
    }

    override suspend fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount) =
        suspendCoroutine<ServiceResult<Unit>> { continuation ->
            try {
                val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
                mAuth.signInWithCredential(credential)
                    .addOnSuccessListener { task ->
                        continuation.resume(ServiceResult.Success(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(ServiceResult.Failure(it))
                    }
            } catch (e: Exception) {
                continuation.resume(ServiceResult.Failure(e))
            }
        }

    private fun signUpGoogleAccount(firebaseUser: FirebaseUser) {
        val email = firebaseUser.email.toString()
        if (email.isNotEmpty()) {
            val user = User()
            user.email = email
            user.userName = email.let {
                it.substring(0, it.indexOf("@"))
            }
            user.userId = FirebaseAuth.getInstance().uid ?: ""

            val newUserReference: DocumentReference = firestore
                .collection(context.getString(R.string.collection_users))
                .document(FirebaseAuth.getInstance().uid!!)
            newUserReference.set(user)
        }
    }

    private suspend fun isAlreadySignUp(): Boolean {
        coroutineScope {
            launch {
                val usersReference =
                    firestore.collection("Users").document(mAuth.currentUser?.uid!!)
                usersReference.get().addOnCompleteListener { document ->
                    Log.d(TAG, "exist")
                }
            }
        }
        // Todo: Implementation.
        return true
    }

    override suspend fun signUpWithEmail(email: String, password: String) =
        suspendCoroutine<ServiceResult<Unit>> { continuation ->
            try {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        continuation.resume(ServiceResult.Success(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(ServiceResult.Failure(it))
                    }
            } catch (e: Exception) {
                continuation.resume(ServiceResult.Failure(e))
            }
        }

    override suspend fun signInWithEmail(email: String, password: String) =
        suspendCoroutine<ServiceResult<Unit>> { continutation ->
            try {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { task ->
                        continutation.resume(ServiceResult.Success(Unit))
                    }
                    .addOnFailureListener {
                        continutation.resume(ServiceResult.Failure(it))
                    }
            } catch (e: Exception) {
                continutation.resume(ServiceResult.Failure(e))
            }
        }

    override suspend fun deleteAccount(userId: String): ServiceResult<Unit> =
        suspendCoroutine { continuation ->
            try {
                mAuth.currentUser?.delete()
                    ?.addOnSuccessListener {
                        continuation.resume(ServiceResult.Success(Unit))
                    }
                    ?.addOnFailureListener {
                        continuation.resume(ServiceResult.Failure(it))
                    }
            } catch (e: Exception) {
                continuation.resume(ServiceResult.Failure(e))
            }
        }
}