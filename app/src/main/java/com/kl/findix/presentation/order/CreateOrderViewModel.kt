package com.kl.findix.presentation.order

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.R
import com.kl.findix.model.Order
import com.kl.findix.model.UserLocation
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.safeLet
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CreateOrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService
): ViewModel() {

    var order = Order()

    var showToastCommand: MutableLiveData<Int> = MutableLiveData()
    var succeedCreateOrderCommand: MutableLiveData<Boolean> = MutableLiveData()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun createOrder(context: Context, locationProviderClient: FusedLocationProviderClient) {
        safeLet(
            order.title?.isNotBlank(),
            order.description?.isNotBlank()
        ) { isFilledTitle, isFilledDescription ->
            if (isFilledTitle && isFilledDescription) {
                firebaseUser?.let { firebaseUser ->
                    viewModelScope.launch {
                        order.hasRegisterLocation?.let {
                            if (it) {
                                order.userLocation = getLocation(context, locationProviderClient)
                            }
                        }
                    }
                    viewModelScope.launch {
                        firebaseDataBaseService.createOrder(
                            firebaseUser,
                            order
                        ) { succeedCreateOrderCommand.postValue(true) }
                    }
                }
            } else {
                showToastCommand.postValue(R.string.error_order_info_not_filled)
            }
        }
    }

    private suspend fun getLocation(
        context: Context,
        locationProviderClient: FusedLocationProviderClient
    ): UserLocation? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return suspendCoroutine<UserLocation> { coroutine ->
                locationProviderClient.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val location = task.result
                        location?.let {
                            coroutine.resume(
                                UserLocation(
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                            )
                        }
                    }
                }
            }
        } else {
            return null
        }
    }
}