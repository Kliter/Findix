package com.kl.findix.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl.findix.model.SignInInfo
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.safeLet
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    val firebaseUserService: FirebaseUserService
): ViewModel() {

    // state
    val signUpResult: MutableLiveData<Boolean> = MutableLiveData()
    val signInResult: MutableLiveData<Boolean> = MutableLiveData()

    var signInInfo: SignInInfo = SignInInfo("", "")

    suspend fun getCurrentSignInUser() = firebaseUserService.getCurrentSignInUser()

    suspend fun signUpWithEmail() {
        safeLet(signInInfo.email, signInInfo.password) { email, password ->
            viewModelScope.launch {
                firebaseUserService.signUpWithEmail(
                    email = email,
                    password = password,
                    emailSignUpSuccessListener = {
                        signUpResult.postValue(true)
                    },
                    emailSignUpFailedListener = {
                        signUpResult.postValue(false)
                    }
                )
            }
        }
    }

    suspend fun signInWithEmail() {
        safeLet(signInInfo.email, signInInfo.password) { email, password ->
            viewModelScope.launch {
                firebaseUserService.signInWithEmail(
                    email = email,
                    password = password,
                    emailSignInSuccessListener = {
                        signInResult.postValue(true)
                    },
                    emailSignInFailedListener = {
                        signInResult.postValue(false)
                    }
                )
            }
        }
    }
}