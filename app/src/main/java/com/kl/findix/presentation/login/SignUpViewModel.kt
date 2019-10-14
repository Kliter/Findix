package com.kl.findix.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kl.findix.model.SignInInfo
import com.kl.findix.services.FirebaseUserService
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    val firebaseUserService: FirebaseUserService
): ViewModel() {

    // state
    val signUpResult: MutableLiveData<Boolean> = MutableLiveData()
    val signInResult: MutableLiveData<Boolean> = MutableLiveData()

    var signInInfo: SignInInfo = SignInInfo("", "")

    fun getCurrentSignInUser() = firebaseUserService.getCurrentSignInUser()

    fun signUpWithEmail() {
        firebaseUserService.signUpWithEmail(
            signInInfo = signInInfo,
            emailSignUpSuccessListener = {
                signUpResult.postValue(true)
            },
            emailSignUpFailedListener = {
                signUpResult.postValue(false)
            }
        )
    }

    fun signInWithEmail() {
            firebaseUserService.signInWithEmail(
                signInInfo = signInInfo,
                emailSignInSuccessListener = {
                    signInResult.postValue(true)
                },
                emailSignInFailedListener = {
                    signInResult.postValue(false)
                }
            )
    }
}