package com.kl.findix.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.Order
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseUserService
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateOrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService
): ViewModel() {

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()


    fun createOrder(order: Order) {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                firebaseDataBaseService.createOrder(firebaseUser, order)
            }
        }
    }
}