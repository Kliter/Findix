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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import com.kl.findix.model.UserLocation
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseUserServiceImpl
import com.kl.findix.services.MapServiceImpl
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val mapService: MapServiceImpl,
    private val firebaseUserService: FirebaseUserServiceImpl,
    private val firebaseDataBaseService: FirebaseDataBaseService
) : ViewModel() {

    companion object {
        private const val TAG = "MapsViewModel"
    }

    var userLocation: MutableLiveData<UserLocation> = MutableLiveData()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

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

    fun updateUserLocation() {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                firebaseDataBaseService.fetchUserLocation(
                    firebaseUser,
                    fetchUserLocationListener = {
                        userLocation.postValue(it)
                    })
            }
        }
    }
}