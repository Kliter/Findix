package com.kl.findix.presentation.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.GeoPoint
import com.kl.findix.services.FirebaseUserServiceImpl
import com.kl.findix.services.MapServiceImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val mapService: MapServiceImpl,
    private val firebaseUserService: FirebaseUserServiceImpl
) : ViewModel() {

    companion object {
        private const val TAG = "MapsViewModel"
    }

    // Event
    val backToLoginCommand: MutableLiveData<Boolean> = MutableLiveData()

    fun getCurrentSignInUser() {
        viewModelScope.launch {
            if (firebaseUserService.getCurrentSignInUser() == null) {
                backToLoginCommand.postValue(true)
            }
        }
    }

    // 仮
    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }

    fun getLastKnownPermission(
        context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    location?.let {
                        val geoPoint = GeoPoint(location.latitude, location.longitude)
                        Log.d(TAG, "onComplete: latitude: ${location.latitude}")
                        Log.d(TAG, "onComplete: longitude: ${location.longitude}")
                    }
                }
            }
        }
    }
}