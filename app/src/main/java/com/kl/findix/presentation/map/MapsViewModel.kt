package com.kl.findix.presentation.map

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.services.FirebaseUserServiceImpl
import com.kl.findix.services.MapServiceImpl
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val mapService: MapServiceImpl,
    private val firebaseUserService: FirebaseUserServiceImpl
) : ViewModel() {

    fun getCurrentSignInUser(): FirebaseUser? {
        return firebaseUserService.getCurrentSignInUser()
    }

    // 仮
    fun signOut() {
        firebaseUserService.signOut()
    }
}