package com.kl.findix.presentation.order

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
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
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CreateOrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService
) : ViewModel() {

    var order: Order? = null

    var showToastCommand: PublishLiveDataKtx<Int> = PublishLiveDataKtx()
    var succeedCreateOrderCommand: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun resetOrderInfo() {
        order = Order()
    }

    fun createOrder(context: Context, locationProviderClient: FusedLocationProviderClient) {
        safeLet(
            order,
            order?.title?.isNotBlank(),
            order?.description?.isNotBlank()
        ) { order, isFilledTitle, isFilledDescription ->
            if (isFilledTitle && isFilledDescription) {
                firebaseUser?.let { firebaseUser ->
                    GlobalScope.launch {
                        order.shouldRegisterLocation?.let {
                            if (it) {
                                order.userLocation = getLocation(context, locationProviderClient)
                            }
                        }
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
            return suspendCoroutine { continuation ->
                locationProviderClient.lastLocation.addOnSuccessListener { result ->
                    result?.let {
                        continuation.resume(
                            UserLocation(
                                latitude = result.latitude,
                                longitude = result.longitude
                            )
                        )
                    }
                }.addOnCanceledListener {
                    continuation.resumeWithException(Exception("Error occur."))
                }
                return@suspendCoroutine
            }
        } else {
            return null
        }
    }
}