package com.andigeeky.finnhub.domain.common

sealed interface Resource<out T> {
    data class Loading<out T>(val data: T? = null) : Resource<T>
    data class Success<out T>(val data: T) : Resource<T>
    data class Error<out T>(val error: FinnError, val data: T? = null) : Resource<T>
}
