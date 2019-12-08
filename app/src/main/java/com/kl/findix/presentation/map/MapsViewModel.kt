package com.kl.findix.presentation.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.UserLocation
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseUserServiceImpl
import com.kl.findix.services.MapServiceImpl
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

    fun moveToCurrentLocation(
        context: Context,
        locationProviderClient: FusedLocationProviderClient
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val location = task.result
                    location?.let {
                        registerUserLocation(location.latitude, location.longitude)
                        userLocation.postValue(UserLocation(location.latitude, location.longitude))
                    }
                }
            }
        }
    }

    private fun registerUserLocation(latitude: Double, longitude: Double) {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                firebaseDataBaseService.updateUserLocation(
                    firebaseUser,
                    UserLocation(latitude, longitude)
                )
            }
        }
    }

    fun fetchUserLocation() {
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