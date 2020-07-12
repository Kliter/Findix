package com.kl.findix.presentation.profiledetail

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.model.Order
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileDetailViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseStorageService: FirebaseStorageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver, UiStateViewModelDelegate by uiStateViewModelDelegate {

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: MutableLiveData<User>
        get() = _user
    private val _orders: MutableLiveData<List<Order>> = MutableLiveData()
    val orders: LiveData<List<Order>>
        get() = _orders

    val setWorkPhotosCommand: MutableLiveData<Pair<Int, StorageReference>> = MutableLiveData()

    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo(userId: String) {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            when (val result = firebaseDataBaseService.fetchUserInfo(userId)) {
                is ServiceResult.Success -> {
                    uiState.postValue(UiState.Loaded)
                    result.data?.let { user ->
                        _user.postValue(user)
                    }
                    fetchOrders(userId)
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
            }
        }
    }

    fun setWorkPhotos() {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                uiState.postValue(UiState.Loading)
                (0..4).forEach {
                    delay(100) // 待たずにpostすると5だけになる
                    val reference = firebaseStorageService.getWorkPhotoRef(firebaseUser.uid, it + 1)
                    setWorkPhotosCommand.postValue(Pair(it + 1, reference))
                }
            }
        }
    }

    private fun fetchOrders(userId: String) {
        uiState.postValue(UiState.Loading)
        viewModelScope.launch {
            uiState.postValue(UiState.Loaded)
            when (val result = firebaseDataBaseService.fetchOrdersByUserId(
                userId = userId,
                lastOrder = null
            )) {
                is ServiceResult.Success -> {
                    uiState.postValue(UiState.Loaded)
                    _orders.postValue(result.data)
                }
                is ServiceResult.Failure -> {
                    handleError(result.exception)
                }
            }
        }
    }
}