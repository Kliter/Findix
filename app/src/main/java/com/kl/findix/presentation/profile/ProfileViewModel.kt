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
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService,
    private val firebaseUserService: FirebaseUserService,
    private val firebaseStorageService: FirebaseStorageService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(), LifecycleObserver,
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    companion object {
        private const val TAG = "ProfileViewModel"
        private val WORK_PHOTO_LIMIT = 5
    }

    // State
    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user
    private val _orders: MutableLiveData<List<Order>> = MutableLiveData()
    val orders: LiveData<List<Order>>
        get() = _orders

    var profileIconBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    // Event
    val showDeleteOrderConfirmDialogCommand: PublishLiveDataKtx<String> = PublishLiveDataKtx()
    val showSnackBarCommand: PublishLiveDataKtx<Int> = PublishLiveDataKtx()
    val setWorkPhotosCommand: PublishLiveDataKtx<Pair<Int, StorageReference>> = PublishLiveDataKtx()

    var index: Int = 0
    private var firebaseUser: FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun fetchUserInfo() {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            firebaseUser?.let { firebaseUser ->
                when (val result =
                    firebaseDataBaseService.fetchOwnProfileInfo(firebaseUser = firebaseUser)) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                        _user.postValue(result.data)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                    }
                }
            }
        }
    }

    fun fetchOwnOrder(lastOrder: Order? = null) {
        firebaseUser?.let { firebaseUser ->
            viewModelScope.launch {
                uiState.postValue(UiState.Loading)
                when (val result = firebaseDataBaseService.fetchOwnOrders(
                    userId = firebaseUser.uid,
                    lastOrder = lastOrder
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

    fun showDeleteOrderConfirm(orderId: String) {
        showDeleteOrderConfirmDialogCommand.postValue(orderId)
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)
            when (val result = firebaseDataBaseService.deleteOrder(orderId = orderId)) {
                is ServiceResult.Success -> {
                    fetchOwnOrder()
                    showSnackBarCommand.postValue(R.string.complete_delete_order)
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
}
