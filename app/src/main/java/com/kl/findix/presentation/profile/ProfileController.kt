package com.kl.findix.presentation.profile

import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemProfileBaseInfoView
import com.kl.findix.itemProfilePrivateInfoView
import com.kl.findix.itemProfileSectionHeaderView
import com.kl.findix.itemProfileUserIcon
import com.kl.findix.model.User

class ProfileController(
    private val onClickUserIcon: () -> Unit
): EpoxyController() {

    var user: User? = null

    override fun buildModels() {
        itemProfileUserIcon {
            id(modelCountBuiltSoFar)
            onClickUserIcon { it ->
                onClickUserIcon.invoke()
            }
        }

        itemProfileBaseInfoView {
            id(modelCountBuiltSoFar)
            user(user)
        }

        itemProfileSectionHeaderView {
            id(modelCountBuiltSoFar)
        }

        itemProfilePrivateInfoView {
            id(modelCountBuiltSoFar)
            user(user)
        }
    }
}