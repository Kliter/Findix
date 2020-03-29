package com.kl.findix.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.SignInInfo
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.UiState
import com.kl.findix.util.delegate.UiStateViewModelDelegate
import com.kl.findix.util.extension.safeLet
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    private val uiStateViewModelDelegate: UiStateViewModelDelegate
) : ViewModel(),
    UiStateViewModelDelegate by uiStateViewModelDelegate {

    val signInResult: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()
    var signInInfo: SignInInfo = SignInInfo("", "")

    fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount?) {
        googleSignInAccount?.let {
            viewModelScope.launch {
                uiState.postValue(UiState.Loading)
                when (val result = firebaseUserService.signInWithGoogle(googleSignInAccount = it)) {
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

    fun signInWithEmail() {
        safeLet(signInInfo.email, signInInfo.password) { email, password ->
            viewModelScope.launch {
                uiState.postValue(UiState.Loading)
                when (val result = firebaseUserService.signInWithEmail(email = email, password = password)) {
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