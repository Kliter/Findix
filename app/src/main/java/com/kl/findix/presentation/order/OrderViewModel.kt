package com.kl.findix.presentation.order

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.ServiceResult
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver,
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    private val _orderListItems: MutableLiveData<Pair<Boolean, List<OrderListItem>>> = MutableLiveData()
    val orderListItems: LiveData<Pair<Boolean, List<OrderListItem>>>
        get() = _orderListItems

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchLast15Orders() {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            when (val result = firebaseDataBaseService.fetchLast15Orders()) {
                is ServiceResult.Success -> {
                    uiState.postValue(UiState.Loaded)
                    val orderListItems = result.data.map { order ->
                        OrderListItem(order = order)
                    }
                    orderListItems.forEach { orderListItem ->
                        orderListItem.order.userId?.let { userId ->
                            orderListItem.orderPhotoRef = firebaseStorageService.getOrderPhotoRef(
                                userId,
                                orderListItem.order.orderId
                            )
                        }
                    }
                    _orderListItems.postValue(Pair(true, orderListItems))
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
            }
        }
    }

    fun fetchAdditional15Orders() {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            val lastOrderTimeStamp = _orderListItems.value?.second?.last()?.order?.timeStamp
            lastOrderTimeStamp?.let {
                when (val result =
                    firebaseDataBaseService.fetchAdditional15Orders(lastOrderTimeStamp)) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                        val orderListItems = result.data.map { order ->
                            OrderListItem(order = order)
                        }
                        orderListItems.forEach { orderListItem ->
                            orderListItem.order.userId?.let { userId ->
                                orderListItem.orderPhotoRef =
                                    firebaseStorageService.getOrderPhotoRef(
                                        userId,
                                        orderListItem.order.orderId
                                    )
                            }
                        }

                        val oldList = _orderListItems.value?.second ?: listOf()
                        val newList = oldList + orderListItems

                        if (orderListItems.isNotEmpty()) {
                            _orderListItems.postValue(Pair(true, newList))
                        } else {
                            _orderListItems.postValue(Pair(false, newList))
                        }
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }
    }
}