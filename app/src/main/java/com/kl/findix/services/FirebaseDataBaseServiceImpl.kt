package com.kl.findix.services

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.kl.findix.model.User
import javax.inject.Inject

class FirebaseDataBaseServiceImpl @Inject constructor(
    private val database: FirebaseFirestore
): FirebaseDataBaseService {
    override fun fetchProfileInfo(firebaseUser: FirebaseUser, fetchProfileInfoListener: (User) -> Unit) {
        database.collection("User").document(firebaseUser.uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.toObject(User::class.java)?.let { user ->
                    fetchProfileInfoListener.invoke(user)
                }
            }
        }
    }

    override fun updateProfileInfo(firebaseUser: FirebaseUser, user: User) {
        database.collection("User").document(firebaseUser.uid).set(user)
    }
}