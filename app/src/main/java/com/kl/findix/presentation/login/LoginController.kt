package com.kl.findix.presentation.login

import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemAppTitleView
import com.kl.findix.itemLoginParameterView
import com.kl.findix.model.SignInInfo

class LoginController(
    private val signInInfo: SignInInfo,
    private val onClickGoogleSignIn: () -> Unit,
    private val onClickEmailSignIn: () -> Unit,
    private val onClickSignUp: () -> Unit
): EpoxyController() {

    override fun buildModels() {
        itemAppTitleView {
            id(modelCountBuiltSoFar)
        }
        itemLoginParameterView {
            id(modelCountBuiltSoFar)
            signInInfo(signInInfo)
            onClickGoogleSign { _ ->
                onClickGoogleSignIn.invoke()
            }
            onClickEmailSignIn { _ ->
                onClickEmailSignIn.invoke()
            }
            onClickSignUp { _ ->
                onClickSignUp.invoke()
            }
        }
    }
}