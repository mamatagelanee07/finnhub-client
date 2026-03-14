package com.andigeeky.finnhub.domain.common

sealed interface Resource<out T> {
    val data: T?
    val error: FinnError?

    data class Loading<out T>(
        override val data: T? = null,
        override val error: FinnError? = null
    ) : Resource<T>

    data class Success<out T>(
        override val data: T,
        override val error: FinnError? = null
    ) : Resource<T>

    data class Error<out T>(
        override val error: FinnError,
        override val data: T? = null
    ) : Resource<T>
}
