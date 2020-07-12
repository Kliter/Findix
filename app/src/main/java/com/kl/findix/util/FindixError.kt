package com.kl.findix.util

sealed class FindixError(
    val alertTitle: String = "Error is occurred.",
    val alertMessage: String = ""
) : Exception() {

    class NetworkError : FindixError(alertMessage = "Please check your network and retry.")
    class UndefinedError : FindixError(alertMessage = "")
    class UserNotFoundError : FindixError(alertMessage = "Sorry, we could not find this user.")
}