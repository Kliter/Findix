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

    suspend fun getCurrentSignInUser(): FirebaseUser? {
        return firebaseUserService.getCurrentSignInUser()
    }

    // 仮
    suspend fun signOut() {
        firebaseUserService.signOut()
    }
}