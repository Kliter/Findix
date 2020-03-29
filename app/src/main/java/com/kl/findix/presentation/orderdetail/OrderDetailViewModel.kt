package com.kl.findix.presentation.orderdetail

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.model.Order
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService
) : ViewModel(), LifecycleObserver {

    // State
    var _order: MutableLiveData<Order> = MutableLiveData()

    // Event
    var navigateToProfileDetailCommand: PublishLiveDataKtx<String> = PublishLiveDataKtx()
    var setOrderPhotoCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchOrderDetail(orderId: String) {
        viewModelScope.launch {
            firebaseDataBaseService.fetchOrderDetail(
                orderId = orderId,
                fetchOrderDetailListener = { order ->
                    _order.postValue(order)
                }
            )
        }
    }

    fun toProfileDetailFragment() {
        _order.value?.userId?.let {
            navigateToProfileDetailCommand.postValue(it)
        }
    }

    fun setOrderPhoto(orderId: String) {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                setOrderPhotoCommand.postValue(
                    firebaseStorageService.getOrderPhotoRef(
                        firebaseUser.uid,
                        orderId
                    )
                )
            }
        }
    }
}