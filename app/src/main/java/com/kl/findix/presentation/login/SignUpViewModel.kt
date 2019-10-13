package com.kl.findix.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kl.findix.R
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.safeLet
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    val firebaseUserService: FirebaseUserService
): ViewModel() {

    // state
    val signUpResult: MutableLiveData<Boolean> = MutableLiveData()
    val signInResult: MutableLiveData<Boolean> = MutableLiveData()

    private var _email: String? = null
    private var _password: String? = null

    fun getCurrentSignInUser() = firebaseUserService.getCurrentSignInUser()

    fun emailSignUp(email: String, password: String) {
        _email = email
        _password = password

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

    fun emailSignIn() {
        safeLet(_email, _password) { email, password ->
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