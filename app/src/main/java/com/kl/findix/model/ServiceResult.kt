package com.kl.findix.model

import com.kl.findix.util.Status

sealed class ServiceResult<out T> {
    data class Success<T>(
        val data: T
    ): ServiceResult<T>()
    data class Failure<T>(
        val error: String
    ): ServiceResult<T>()
}