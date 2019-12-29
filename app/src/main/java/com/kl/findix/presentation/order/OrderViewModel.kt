package com.kl.findix.presentation.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kl.findix.model.Order
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseUserService
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService
): ViewModel() {

    val orders: MutableLiveData<List<Order>> = MutableLiveData()

    init {
//        orders = firebaseDataBaseService.fetchOrders()
    }

}