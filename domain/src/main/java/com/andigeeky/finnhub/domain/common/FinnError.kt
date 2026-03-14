package com.andigeeky.finnhub.domain.common

sealed class FinnError {
    data object Network : FinnError()
    data class Server(val code: Int, val message: String? = null) : FinnError()
    data class Client(val code: Int, val message: String? = null) : FinnError()
    data class Unknown(val message: String? = null) : FinnError()
}