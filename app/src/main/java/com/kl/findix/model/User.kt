package com.kl.findix.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class User(): BaseObservable() {

    constructor(
        userName: String? = "",
        userId: String? = "",
        company: String? = "",
        major: String? = "",
        description: String? = "",
        website: String? = "",
        email: String? = "",
        phone: String? = "",
        profilePhotoUrl: String? = ""
    ): this() {
        this.userName = userName
        this.userId = userId
        this.company = company
        this.major = major
        this.description = description
        this.website = website
        this.email = email
        this.phone = phone
        this.profilePhotoUrl = profilePhotoUrl
    }

    @get:Bindable
    var userName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.userName)
        }
    @Bindable
    var userId: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.userId)
        }

    @Bindable
    var company: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.company)
        }

    @Bindable
    var major: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.major)
        }
    @Bindable
    var description: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }
    @Bindable
    var website: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.website)
        }
    @Bindable
    var email: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }
    @Bindable
    var phone: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.phone)
        }

    var profilePhotoUrl: String? = ""

}