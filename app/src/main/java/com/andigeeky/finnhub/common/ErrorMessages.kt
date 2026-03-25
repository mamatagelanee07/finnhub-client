package com.andigeeky.finnhub.common

import com.andigeeky.finnhub.domain.common.FinnError

fun FinnError.getMessage() = when(this){
    is FinnError.Client -> this.message
    FinnError.Network -> "Network Error"
    is FinnError.Server -> this.message
    is FinnError.Unknown -> this.message
}