package com.andigeeky.finnhub.data.common

sealed interface NetworkResponse<T> {
    data class Success<T>(val response: T) : NetworkResponse<T>

    sealed interface Failure<T> : NetworkResponse<T> {
        val message: String

        data class NoInternet<T>(override val message: String) : Failure<T>
        data class ClientError<T>(val code: ClientCode, override val message: String) : Failure<T>
        data class ServerError<T>(val code: ServerCode, override val message: String) : Failure<T>
        data class UnexpectedError<T>(
            override val message: String,
            val throwable: Throwable? = null
        ) : Failure<T>
    }
}

@JvmInline
value class ClientCode(val value: Int) {
    init {
        require(value in 400..499) { "Client error code must be between 400 and 499, but was $value" }
    }
}

@JvmInline
value class ServerCode(val value: Int) {
    init {
        require(value in 500..599) { "Server error code must be between 500 and 599, but was $value" }
    }
}
