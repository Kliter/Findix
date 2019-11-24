package com.kl.findix.presentation.profile

import android.graphics.Bitmap
import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemProfileBaseInfoView
import com.kl.findix.itemProfilePhoto
import com.kl.findix.itemProfilePrivateInfoView
import com.kl.findix.itemProfileSectionHeaderView
import com.kl.findix.model.User

class ProfileController(
    private val onClickUserIcon: () -> Unit
): EpoxyController() {

    var user: User? = null
    var profilePhotoSrc: Bitmap? = null

    override fun buildModels() {
        itemProfilePhoto {
            id(modelCountBuiltSoFar)
            profilePhotoSrc(profilePhotoSrc)
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