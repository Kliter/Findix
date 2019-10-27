package com.kl.findix.presentation.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseUserService
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService
): ViewModel() {

    var user: User = User()

    private var myRef = FirebaseDatabase.getInstance().reference
    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun saveProfileSettings() {
        firebaseUser?.let {
            myRef.child("users").child(it.uid).setValue(user)
        }
    }
}