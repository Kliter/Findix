package com.kl.findix.presentation.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.Order
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseUserService
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService
): ViewModel() {

    val orders: MutableLiveData<List<Order>> = MutableLiveData()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    init {
        fetchLast15Orders()
    }

    private fun fetchLast15Orders() {
        viewModelScope.launch {
            firebaseDataBaseService.fetchLast15Orders(
                fetchLast15OrdersListener = { result ->
                    orders.postValue(result)
                })
        }
    }

    fun createOrder(order: Order) {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                firebaseDataBaseService.createOrder(firebaseUser, order)
            }
        }
    }
}