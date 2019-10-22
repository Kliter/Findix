package com.kl.findix.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl.findix.services.FirebaseUserServiceImpl
import com.kl.findix.services.MapServiceImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    private val mapService: MapServiceImpl,
    private val firebaseUserService: FirebaseUserServiceImpl
) : ViewModel() {

    // Event
    val backToLoginCommand: MutableLiveData<Boolean> = MutableLiveData()

    fun getCurrentSignInUser() {
        viewModelScope.launch {
            if (firebaseUserService.getCurrentSignInUser() == null) {
                backToLoginCommand.postValue(true)
            }
        }
    }

    // 仮
    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }
}