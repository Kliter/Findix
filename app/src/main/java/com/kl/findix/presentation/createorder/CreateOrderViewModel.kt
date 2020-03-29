package com.kl.findix.presentation.createorder

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.R
import com.kl.findix.model.CityNumber
import com.kl.findix.model.Order
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.UserLocation
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.services.ImageService
import com.kl.findix.util.extension.safeLet
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CreateOrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService,
    private val imageService: ImageService
) : ViewModel() {

    companion object {
        private const val TAG = "CreateOrderViewModel"
    }

    var orderPhotoBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    var showToastCommand: PublishLiveDataKtx<Int> = PublishLiveDataKtx()
    var succeedCreateOrderCommand: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()
    var setOrderPhotoCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()
    private var _orderPhotoUri: Uri? = null
    var order: Order? = null
    var cityNumber: CityNumber? = null // Spinnerのテキストバインドするために必要

    fun resetOrderInfo() {
        order = Order()
        cityNumber = CityNumber()
        _orderPhotoUri = null
    }

    fun createOrder(
        context: Context,
        locationProviderClient: FusedLocationProviderClient,
        contentResolver: ContentResolver
    ) {
        safeLet(
            order,
            order?.title?.isNotBlank(),
            order?.description?.isNotBlank()
        ) { order, isFilledTitle, isFilledDescription ->
            if (isFilledTitle && isFilledDescription) {
                cityNumber?.number?.let { number ->
                    order.apply {
                        this.city = context.resources.getStringArray(R.array.cities)[number]
                    }
                }
                order.hasPhoto = _orderPhotoUri != null
                firebaseUser?.let { firebaseUser ->
                    GlobalScope.launch {
                        // viewModelScopeだとちゃんとScope破棄できなくて2回目createできない。
                        order.shouldRegisterLocation?.let {
                            if (it) {
                                order.userLocation = getLocation(context, locationProviderClient)
                            }
                        }
                        firebaseDataBaseService.createOrder(
                            firebaseUser,
                            order
                        ) { orderId ->
                            succeedCreateOrderCommand.postValue(true)
                            uploadOrderPhoto(orderId, contentResolver)
                        }
                    }
                }
            } else {
                showToastCommand.postValue(R.string.error_order_info_not_filled)
            }
        }
    }

    private fun uploadOrderPhoto(orderId: String, contentResolver: ContentResolver) {
        GlobalScope.launch {
            safeLet(
                firebaseUserService.getCurrentSignInUser(),
                _orderPhotoUri
            ) { currentSignInUser, orderPhotoUri ->
                GlobalScope.launch {
                    when (val result = imageService.getBitmap(orderPhotoUri, contentResolver)) {
                        is ServiceResult.Success -> {
                            firebaseStorageService.uploadOrderPhoto(
                                userId = currentSignInUser.uid,
                                orderId = orderId,
                                byteArray = imageService.getBytesFromBitmap(result.data)
                            )
                            resetOrderInfo()
                        }
                        is ServiceResult.Failure -> {
                            // TODO: Error handling.
                        }
                    }
                }
            }
        }
    }

    fun updateOrderPhoto(uri: Uri, contentResolver: ContentResolver) {
        when (val result = imageService.getBitmap(uri, contentResolver)) {
            is ServiceResult.Success -> {
                _orderPhotoUri = uri
                orderPhotoBitmap.postValue(result.data)
            }
            is ServiceResult.Failure -> {
                // TODO: Error handling.
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