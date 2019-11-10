package com.kl.findix.services

import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.User

interface FirebaseDataBaseService {
    fun fetchProfileInfo(firebaseUser: FirebaseUser,  fetchProfileInfoListener: (User) -> Unit)
    fun updateProfileInfo(firebaseUser: FirebaseUser, user: User)
}