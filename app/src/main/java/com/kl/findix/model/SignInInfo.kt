package com.kl.findix.model

import androidx.annotation.Keep
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

@Keep
class SignInInfo() : BaseObservable() {

    constructor(email: String, password: String): this() {
        this.email = email
        this.password = password
    }

    @get:Bindable
    var email: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    @get:Bindable
    var password: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }
}