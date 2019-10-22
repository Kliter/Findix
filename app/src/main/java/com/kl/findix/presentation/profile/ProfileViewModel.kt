package com.kl.findix.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseUserService
import kotlinx.coroutines.async
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    val firebaseUserServce: FirebaseUserService
): ViewModel() {

    var user: User = User()

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("User")
    private var firebaseUser: FirebaseUser? = firebaseUserServce.getCurrentSignInUser()

    fun saveProfileSettings() {
        firebaseUser?.let {
            myRef.child("users").child(it.uid).setValue(user)
        }
    }
}