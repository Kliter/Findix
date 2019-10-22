package com.kl.findix.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.SignInInfo
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.safeLet
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService
): ViewModel() {

    val signInResult: MutableLiveData<Boolean> = MutableLiveData()
    var signInInfo: SignInInfo = SignInInfo("", "")

    fun signOut() {
        viewModelScope.launch {
            firebaseUserService.signOut()
        }
    }

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

    fun isSignedIn() {
        viewModelScope.launch {
            signInResult.postValue(firebaseUserService.getCurrentSignInUser() != null)
        }
    }
}