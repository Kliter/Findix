package com.kl.findix.util

sealed class FindixError(
    val alertTitle: String = "Error is occurred.",
    val alertMessage: String = ""
) {

    class NetworkError: FindixError(alertMessage = "Please check your network and retry.")
    class UndefinedError: FindixError(alertMessage = "")
}