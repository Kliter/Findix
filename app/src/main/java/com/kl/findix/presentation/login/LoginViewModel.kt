package com.kl.findix.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.kl.findix.model.SignInInfo
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.util.safeLet
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService
): ViewModel() {

    val signInResult: MutableLiveData<Boolean> = MutableLiveData()
    var signInInfo: SignInInfo = SignInInfo("", "")

    fun getCurrentSignInUser(): FirebaseUser? = firebaseUserService.getCurrentSignInUser()

    fun signOut() {
        firebaseUserService.signOut()
    }

    fun signInWithGoogle(googleSignInAccount: GoogleSignInAccount?) {
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

    fun signInWithEmail() {
        safeLet(signInInfo.email, signInInfo.password) { email, password ->
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

    fun isSignedIn() {
        signInResult.postValue(firebaseUserService.getCurrentSignInUser() != null)
    }
}