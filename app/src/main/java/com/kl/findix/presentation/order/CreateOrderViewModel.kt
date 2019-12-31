package com.kl.findix.presentation.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.R
import com.kl.findix.model.Order
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.safeLet
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateOrderViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService
): ViewModel() {

    var order = Order()

    var showToastCommand: MutableLiveData<Int> = MutableLiveData()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun createOrder() {
        safeLet(
            order.title?.isNotBlank(),
            order.description?.isNotBlank()
        ) { isFilledTitle, isFilledDescription ->
            if (isFilledTitle && isFilledDescription) {
                firebaseUser?.let { firebaseUser ->
                    viewModelScope.launch {
                        firebaseDataBaseService.createOrder(firebaseUser, order)
                    }
                }
            } else {
                showToastCommand.postValue(R.string.error_order_info_not_filled)
            }
        }
    }
}