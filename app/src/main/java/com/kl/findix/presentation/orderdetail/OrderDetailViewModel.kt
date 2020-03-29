package com.kl.findix.presentation.orderdetail

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.model.Order
import com.kl.findix.model.ServiceResult
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver,
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    // State
    var _order: MutableLiveData<Order> = MutableLiveData()

    // Event
    var navigateToProfileDetailCommand: PublishLiveDataKtx<String> = PublishLiveDataKtx()
    var setOrderPhotoCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchOrderDetail(orderId: String) {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            when (val result = firebaseDataBaseService.fetchOrderDetail(orderId = orderId)) {
                is ServiceResult.Success -> {
                    uiState.postValue(UiState.Loaded)
                    result.data?.let { order ->
                        _order.postValue(order)
                    }
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
            }
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