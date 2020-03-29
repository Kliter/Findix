package com.kl.findix.presentation.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.SignInInfo
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.kl.findix.util.extension.safeLet
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    val firebaseUserService: FirebaseUserService,
    val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(),
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    // state
    val signUpResult: MutableLiveData<Boolean> = MutableLiveData()
    val signInResult: MutableLiveData<Boolean> = MutableLiveData()

    var signInInfo: SignInInfo = SignInInfo("", "")

    fun signUpWithEmail() {
        safeLet(
            signInInfo.email,
            signInInfo.password
        ) { email, password ->
            viewModelScope.launch {
                uiState.postValue(UiState.Loading)
                when (val result = firebaseUserService.signUpWithEmail(email = email, password = password)) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                        signUpResult.postValue(true)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                        signUpResult.postValue(false)
                    }
                }
            }
        }
    }

    fun signInWithEmail() {
        safeLet(signInInfo.email, signInInfo.password) { email, password ->
            viewModelScope.launch {
                uiState.postValue(UiState.Loading)
                when (val result =
                    firebaseUserService.signInWithEmail(email = email, password = password)) {
                    is ServiceResult.Success -> {
                        uiState.postValue(UiState.Loaded)
                        signInResult.postValue(true)
                    }
                    is ServiceResult.Failure -> {
                        handleError(result.exception)
                        signInResult.postValue(false)
                    }
                }
            }
        }
    }
}