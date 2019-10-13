package com.kl.findix.model

import androidx.databinding.ObservableField

class SignInInfo(
    email: String,
    password: String
) {

    var _email = ObservableField<String>()
    var _password = ObservableField<String>()

    init {
        _email.set(email)
        _password.set(password)
    }

    fun setEmail(email: String) {
        _email.set(email)
    }

    fun setPassword(password: String) {
        _password.set(password)
    }
}