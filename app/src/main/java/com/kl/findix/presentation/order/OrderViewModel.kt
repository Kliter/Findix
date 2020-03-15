package com.kl.findix.presentation.order

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService
) : ViewModel(), LifecycleObserver {

    val orderListItems: MutableLiveData<List<OrderListItem>> = MutableLiveData()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchLast15Orders() {
        viewModelScope.launch {
            firebaseDataBaseService.fetchLast15Orders(
                fetchLast15OrdersListener = { result ->
                    val orderListItems = result.map { order ->
                        OrderListItem(order = order)
                    }
                    setOrderPhotoRef(orderListItems)
                }
            )
        }
    }

    private fun setOrderPhotoRef(list: List<OrderListItem>) {
        firebaseUser?.let { firebaseUser ->
            list.forEach { orderListItem ->
                orderListItem.orderPhotoRef = firebaseStorageService.getOrderPhotoRef(
                    firebaseUser.uid,
                    orderListItem.order.orderId
                )
            }
            orderListItems.postValue(list)
        }
    }
}