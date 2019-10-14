package com.kl.findix.presentation.login

import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemAppTitleView
import com.kl.findix.itemSignupParameterView
import com.kl.findix.model.SignInInfo
import kotlin.math.sign

class SignUpController(
    private val signInInfo: SignInInfo,
    private val onClickSignUp: () -> Unit
): EpoxyController() {

    override fun buildModels() {
        itemAppTitleView {
            id(modelCountBuiltSoFar)
        }
        itemSignupParameterView {
            id(modelCountBuiltSoFar)
            signInInfo(signInInfo)
            onClickSignUp { _ ->
                onClickSignUp.invoke()
            }
        }
    }
}