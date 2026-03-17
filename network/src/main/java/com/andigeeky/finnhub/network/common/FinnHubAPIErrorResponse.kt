package com.andigeeky.finnhub.network.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FinnHubAPIErrorResponse(
    @SerialName("error") val message: String?
)
