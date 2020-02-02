package com.kl.findix.presentation.order

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.User
import com.kl.findix.services.FirebaseDataBaseService
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileDetailViewModel @Inject constructor(
    private val firebaseDataBaseService: FirebaseDataBaseService
) : ViewModel(), LifecycleObserver {

    private val _user: MediatorLiveData<User> = MediatorLiveData()
    val user: MutableLiveData<User>
        get() = _user

    fun fetchUserInfo(userId: String) {
        viewModelScope.launch {

        }
    }
}