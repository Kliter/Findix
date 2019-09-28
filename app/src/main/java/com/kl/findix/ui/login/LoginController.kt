package com.kl.findix.ui.login

import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemAppTitleView
import com.kl.findix.itemLoginParameterView

class LoginController(
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