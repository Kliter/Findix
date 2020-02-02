package com.kl.findix.presentation.order

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kl.findix.model.User
import javax.inject.Inject

class ProfileDetailViewModel @Inject constructor(

): ViewModel(), LifecycleObserver {

    var user: MutableLiveData<User> = MutableLiveData()

    fun fetch() {

    }
}