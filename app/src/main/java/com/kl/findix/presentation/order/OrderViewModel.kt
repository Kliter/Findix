package com.kl.findix.presentation.order

import androidx.lifecycle.LifecycleObserver
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
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver,
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    val orderListItems: MutableLiveData<List<OrderListItem>> = MutableLiveData()

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
                    setOrderPhotoRef(orderListItems)
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
            }
        }
    }

    private fun setOrderPhotoRef(list: List<OrderListItem>) {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            list.forEach { orderListItem ->
                orderListItem.order.userId?.let { userId ->
                    orderListItem.orderPhotoRef = firebaseStorageService.getOrderPhotoRef(
                        userId,
                        orderListItem.order.orderId
                    )
                }
            }
            orderListItems.postValue(list)
        }
    }
}