package com.kl.findix.model

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ServiceResult<out R> {

    data class Success<out T>(val data: T) : ServiceResult<T>()
    data class Failure(val exception: Exception) : ServiceResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Error[exception=$exception]"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is ServiceResult.Success<*> && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? ServiceResult.Success<T>)?.data ?: fallback
}