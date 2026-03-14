package com.andigeeky.finnhub.domain.common

sealed class Resource<out T>(
    open val data: T? = null,
    open val error: FinnError? = null
) {
    data class Loading<out T>(override val data: T? = null) : Resource<T>(data)
    data class Success<out T>(
        override val data: T,
        val isStaleData: Boolean = false
    ) : Resource<T>(data)

    data class Error<out T>(
        override val error: FinnError,
        override val data: T? = null
    ) : Resource<T>(data, error)
}
