package com.kl.findix.presentation.profile

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import com.kl.findix.R
import com.kl.findix.model.Order
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.FindixError.NetworkError
import com.kl.findix.util.FindixError.UndefinedError
import com.kl.findix.util.UiState
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseUserService: FirebaseUserService,
    private val firebaseStorageService: FirebaseStorageService
) : ViewModel(), LifecycleObserver {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    // State
    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user
    private val _orders: MutableLiveData<List<Order>> = MutableLiveData()
    val orders: LiveData<List<Order>>
        get() = _orders
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    val uiState: LiveData<UiState>
        get() = _uiState
    var profileIconBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    // Event
    var setProfileIconCommand: PublishLiveDataKtx<StorageReference> = PublishLiveDataKtx()
    val showDeleteOrderConfirmDialogCommand: PublishLiveDataKtx<String> = PublishLiveDataKtx()
    val showSnackBarCommand: PublishLiveDataKtx<Int> = PublishLiveDataKtx()
    val showErrorDialogCommand: PublishLiveDataKtx<String> = PublishLiveDataKtx()

    var index: Int = 0
    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo() {
        viewModelScope.launch {
            _uiState.postValue(UiState.Loading)
            firebaseUser?.let { firebaseUser ->
                when (val result =
                    firebaseDataBaseService.fetchOwnProfileInfo(firebaseUser = firebaseUser)) {
                    is ServiceResult.Success -> {
                        _uiState.postValue(UiState.Loaded)
                        _user.postValue(result.data)
                    }
                    is ServiceResult.Failure -> {
                        when (val exception = result.exception) {
                            is NetworkError -> {
                                _uiState.postValue(UiState.Retry)
                            }
                            is UndefinedError -> {
                                _uiState.postValue(UiState.Error)
                                showErrorDialogCommand.postValue(exception.alertMessage)
                            }
                            else -> {
                                _uiState.postValue(UiState.Error)
                                exception.message?.let { message ->
                                    showErrorDialogCommand.postValue(message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun setProfileIcon() {
        firebaseUser?.let { firebaseUser ->
            setProfileIconCommand.postValue(firebaseStorageService.getProfileIconRef(firebaseUser.uid))
        }
    }

    fun fetchOwnOrder(lastOrder: Order? = null) {
        viewModelScope.launch {
            firebaseUser?.let { firebaseUser ->
                firebaseDataBaseService.fetchOwnOrders(
                    userId = firebaseUser.uid,
                    lastOrder = lastOrder,
                    fetchOwnOrdersListener = { orders ->
                        _orders.postValue(orders)
                    }
                )
            }
        }
    }

    fun showDeleteOrderConfirm(orderId: String) {
        showDeleteOrderConfirmDialogCommand.postValue(orderId)
    }

    fun deleteOrder(orderId: String) {
        GlobalScope.launch {
            firebaseDataBaseService.deleteOrder(
                orderId = orderId
            ) {
                firebaseUser?.uid?.let { firebaseuserId ->
                    firebaseStorageService.deleteOrderPhoto(
                        userId = firebaseuserId,
                        orderId = orderId
                    )
                }
                fetchOwnOrder()
                showSnackBarCommand.postValue(R.string.complete_delete_order)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }
}
