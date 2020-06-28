package com.kl.findix.presentation.createorder

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.kl.findix.util.extension.safeLet
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CreateOrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService,
    private val imageService: ImageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver,
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    companion object {
        private const val TAG = "CreateOrderViewModel"
    }

    val title: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val shouldRegisterLocation: MutableLiveData<Boolean> = MutableLiveData()

    var orderPhotoBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    val isEnableToSaveOrder: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        this.addSource(title) {
            this.postValue(isFilledRequiredParams())
        }
        this.addSource(description) {
            this.postValue(isFilledRequiredParams())
        }
        this.addSource(name) {
            this.postValue(isFilledRequiredParams())
        }
        this.addSource(orderPhotoBitmap) {
            this.postValue(isFilledRequiredParams())
        }
    }

    var showToastCommand: PublishLiveDataKtx<Int> = PublishLiveDataKtx()
    var succeedCreateOrderCommand: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()
    var setOrderPhotoCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()
    private var _orderPhotoUri: Uri? = null
    var cityNumber: CityNumber? = null // Spinnerのテキストバインドするために必要

    fun resetOrderInfo() {
        cityNumber = CityNumber()
        _orderPhotoUri = null
    }

    fun createOrder(
        context: Context,
        locationProviderClient: FusedLocationProviderClient,
        contentResolver: ContentResolver
    ) {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                uiState.postValue(UiState.Loading)
                val order = Order()
                order.title = title.value
                order.description = description.value
                order.userName = name.value
                cityNumber?.number?.let { number ->
                    order.city = context.resources.getStringArray(R.array.cities)[number]
                }
                if (order.shouldRegisterLocation == true) {
                    order.userLocation = getLocation(context, locationProviderClient)
                }

                when (val result = firebaseDataBaseService.createOrder(firebaseUser, order)) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                        succeedCreateOrderCommand.postValue(true)
                        prepareExecuteUploadOrderPhoto(result.data, contentResolver)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }
    }

    private fun prepareExecuteUploadOrderPhoto(orderId: String, contentResolver: ContentResolver) {
        safeLet(
            firebaseUserService.getCurrentSignInUser(),
            _orderPhotoUri
        ) { currentSignInUser, orderPhotoUri ->
            viewModelScope.launch {
                uiState.postValue(UiState.Loading)
                when (val result = imageService.getBitmap(orderPhotoUri, contentResolver)) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                        executeUploadOrderPhoto(currentSignInUser.uid, orderId, result.data)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }
    }

    private fun executeUploadOrderPhoto(userId: String, orderId: String, orderPhotoBitmap: Bitmap) {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            when (val result = imageService.getBytesFromBitmap(orderPhotoBitmap)) {
                is ServiceResult.Success -> {
                    uploadOrderPhoto(userId, orderId, result.data)
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
            }
        }
    }

    private fun uploadOrderPhoto(userId: String, orderId: String, orderPhotoByteArray: ByteArray) {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            when (val result = firebaseStorageService.uploadOrderPhoto(
                userId = userId,
                orderId = orderId,
                byteArray = orderPhotoByteArray
            )) {
                is ServiceResult.Success -> {
                    uiState.postValue(UiState.Loaded)
                    resetOrderInfo()
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
            }
        }
    }

    fun updateOrderPhoto(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            when (val result = imageService.getBitmap(uri, contentResolver)) {
                is ServiceResult.Success -> {
                    uiState.postValue(UiState.Loaded)
                    _orderPhotoUri = uri
                    orderPhotoBitmap.postValue(result.data)
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
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

    private fun isFilledRequiredParams(): Boolean = title.value?.isNotEmpty() == true
            && description.value?.isNotEmpty() == true
            && name.value?.isNotEmpty() == true
            && orderPhotoBitmap.value != null
}