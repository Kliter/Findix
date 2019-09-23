package com.kl.findix.ui.map

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import com.kl.findix.services.FirebaseUserServiceImpl
import com.kl.findix.util.Status.*
import com.kl.findix.services.MapServiceImpl
import com.kl.findix.util.Resource
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