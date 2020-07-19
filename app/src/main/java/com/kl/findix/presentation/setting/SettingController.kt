package com.kl.findix.presentation.setting

import com.airbnb.epoxy.EpoxyController
import com.kl.findix.itemSettingSectionHeader
import com.kl.findix.itemSettingSelectorList

class SettingController(
    private val onClickSignOut: () -> Unit,
    private val onClickContactUs: () -> Unit,
    private val onClickPrivacyPolicy: () -> Unit,
    private val onClickDeleteAccount: () -> Unit,
    private val onClickLicences: () -> Unit
) : EpoxyController() {
    override fun buildModels() {
        itemSettingSectionHeader {
            id(modelCountBuiltSoFar)
            headerText("About Your Account")
        }
        itemSettingSelectorList {
            id(modelCountBuiltSoFar)
            selectorText("Sign Out")
            onClickList { _ ->
                onClickSignOut.invoke()
            }
        }
        itemSettingSelectorList {
            id(modelCountBuiltSoFar)
            selectorText("Delete Account")
            onClickList { _ ->
                onClickDeleteAccount.invoke()
            }
        }
        itemSettingSectionHeader {
            id(modelCountBuiltSoFar)
            headerText("About This App")
        }
        itemSettingSelectorList {
            id(modelCountBuiltSoFar)
            selectorText("Contact Us")
            onClickList { _ ->
                onClickContactUs.invoke()
            }
        }
        itemSettingSelectorList {
            id(modelCountBuiltSoFar)
            selectorText("Privacy Policy")
            onClickList { _ ->
                onClickPrivacyPolicy.invoke()
            }
        }
        itemSettingSelectorList {
            id(modelCountBuiltSoFar)
            selectorText("Licences")
            onClickList { _ ->
                onClickLicences.invoke()
            }
        }
        itemSettingSelectorList {
            id(modelCountBuiltSoFar)
            selectorText("App Version")
        }
    }
}