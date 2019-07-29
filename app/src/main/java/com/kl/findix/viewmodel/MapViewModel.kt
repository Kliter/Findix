package com.kl.findix.viewmodel

import androidx.lifecycle.ViewModel
import com.kl.findix.firestore.UserService
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val userService: UserService
): ViewModel() {



}