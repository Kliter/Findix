package com.kl.findix.presentation.order

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl.findix.model.Order
import com.kl.findix.services.FirebaseDataBaseService
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService
) : ViewModel(), LifecycleObserver {

    var _order: MutableLiveData<Order> = MutableLiveData()

    var toProfileDetailCommand: PublishLiveDataKtx<String> = PublishLiveDataKtx()

    fun fetchOrderDetail(orderId: String) {
        GlobalScope.launch {
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
            toProfileDetailCommand.postValue(it)
        }
    }
}