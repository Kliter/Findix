package com.kl.findix.presentation.profile

import android.graphics.drawable.Drawable
import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemProfileSectionHeaderView
import com.kl.findix.itemUserIcon
import com.kl.findix.itemUserinfoEmailInputView
import com.kl.findix.itemUserinfoInputView
import com.kl.findix.itemUserinfoPhoneNumberInputView
import com.kl.findix.model.User

class ProfileController(
    private val user: User,
    private val userNameHint: String,
    private val userNameIconRes: Drawable?,
    private val majorHint: String,
    private val majorIconRes: Drawable?,
    private val descriptionHint: String,
    private val descriptionIconRes: Drawable?,
    private val websiteHint: String,
    private val websiteIconRes: Drawable?
): EpoxyController() {

    override fun buildModels() {
        itemUserIcon {
            id(modelCountBuiltSoFar)
            // Todo: Pass the parameter of user icon.
        }

        itemUserinfoInputView {
            id(modelCountBuiltSoFar)
            parameter(user.userName)
            hint(userNameHint)
            userNameIconRes?.let {
                iconRes(it)
            }
        }
        itemUserinfoInputView {
            id(modelCountBuiltSoFar)
            parameter(user.major)
            hint(majorHint)
            majorIconRes?.let {
                iconRes(it)
            }
        }
        itemUserinfoInputView {
            id(modelCountBuiltSoFar)
            parameter(user.description)
            hint(descriptionHint)
            descriptionIconRes?.let {
                iconRes(it)
            }
        }
        itemUserinfoInputView {
            id(modelCountBuiltSoFar)
            parameter(user.website)
            hint(websiteHint)
            websiteIconRes?.let {
                iconRes(it)
            }
        }

        itemProfileSectionHeaderView {
            id(modelCountBuiltSoFar)
        }

        itemUserinfoEmailInputView {
            id(modelCountBuiltSoFar)
            parameter(user.email)
        }
        itemUserinfoPhoneNumberInputView {
            id(modelCountBuiltSoFar)
            parameter(user.phone)
        }
    }
}