package com.kl.findix.presentation.login

import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemAppTitleView
import com.kl.findix.itemSignupParameterView

class SignUpController(
    private val onClickSignUp: () -> Unit
): EpoxyController() {

    override fun buildModels() {
        itemAppTitleView {
            id(modelCountBuiltSoFar)
        }
        itemSignupParameterView {
            id(modelCountBuiltSoFar)
            onClickSignUp { _ ->
                onClickSignUp.invoke()
            }
        }
    }
}