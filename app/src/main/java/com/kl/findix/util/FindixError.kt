package com.kl.findix.util

import java.lang.Exception

sealed class FindixError(
    val alertTitle: String = "Error is occurred.",
    val alertMessage: String = ""
): Exception() {

    class NetworkError: FindixError(alertMessage = "Please check your network and retry.")
    class UndefinedError: FindixError(alertMessage = "")
}