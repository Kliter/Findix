package com.kl.findix.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kl.findix.model.SignInInfo
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.extension.safeLet
import com.shopify.livedataktx.PublishLiveDataKtx
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService
) : ViewModel() {

    val signInResult: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()
    val isAlreadySignedIn: PublishLiveDataKtx<Boolean> = PublishLiveDataKtx()
    var signInInfo: SignInInfo = SignInInfo("", "")

    fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount?) {
        viewModelScope.launch {
            googleSignInAccount?.let {
                firebaseUserService.signInWithGoogle(
                    googleSignInAccount = it,
                    googleSignInSuccessListener = {
                        signInResult.postValue(true)
                    },
                    googleSignInFailedListener = {
                        signInResult.postValue(false)
                    }
                )
            }
        }
    }

    fun signInWithEmail() {
        safeLet(
            signInInfo.email,
            signInInfo.password
        ) { email, password ->
            safeLet(
                email,
                password
            ) { email, password ->
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

    fun isAlreadySignedIn() {
        viewModelScope.launch {
            isAlreadySignedIn.postValue(firebaseUserService.getCurrentSignInUser() != null)
        }
    }
}