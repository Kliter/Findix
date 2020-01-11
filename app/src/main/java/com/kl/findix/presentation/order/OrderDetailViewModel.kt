package com.kl.findix.presentation.order

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl.findix.model.Order
import com.kl.findix.services.FirebaseDataBaseService
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService
) : ViewModel(), LifecycleObserver {

    var _order: MutableLiveData<Order> = MutableLiveData()

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
}