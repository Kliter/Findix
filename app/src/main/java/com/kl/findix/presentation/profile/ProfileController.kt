package com.kl.findix.presentation.profile

import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemProfileBaseInfoView
import com.kl.findix.itemProfilePrivateInfoView
import com.kl.findix.itemProfileSectionHeaderView
import com.kl.findix.itemUserIcon
import com.kl.findix.model.User

class ProfileController(
    private val user: User
): EpoxyController() {

    override fun buildModels() {
        itemUserIcon {
            id(modelCountBuiltSoFar)
            // Todo: Pass the parameter of user icon.
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