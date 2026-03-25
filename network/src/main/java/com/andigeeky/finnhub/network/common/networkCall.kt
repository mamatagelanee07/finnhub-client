package com.andigeeky.finnhub.network.common

import com.andigeeky.finnhub.data.common.ClientCode
import com.andigeeky.finnhub.data.common.NetworkResponse
import com.andigeeky.finnhub.data.common.ServerCode
import kotlinx.serialization.json.Json
import okio.IOException
import retrofit2.Response

internal suspend inline fun <Out, In> networkCall(
    crossinline network: suspend () -> Response<In>,
    crossinline map: suspend (body: In) -> Out,
): NetworkResponse<Out> {
    return try {
        val response = network()
        val body = response.body()
        val httpCode = response.code()
        val errorMessage by lazy { response.getErrorMessage() }
        when {
            response.isSuccessful && body != null -> NetworkResponse.Success(map(body))
            response.isSuccessful && body == null -> NetworkResponse.Failure.UnexpectedError(
                "Empty response body"
            )

            response.isSuccessful.not() && httpCode in 400..499 -> NetworkResponse.Failure.ClientError(
                ClientCode(httpCode), errorMessage
            )

            response.isSuccessful.not() && httpCode in 500..599 -> NetworkResponse.Failure.ServerError(
                ServerCode(httpCode), errorMessage
            )

            else -> NetworkResponse.Failure.UnexpectedError(errorMessage)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        NetworkResponse.Failure.NoInternet(e.message ?: "No internet connection")
    } catch (e: Exception) {
        e.printStackTrace()
        NetworkResponse.Failure.UnexpectedError(e.message ?: "Unknown error")
    }
}

internal val json = Json { ignoreUnknownKeys = true }

/**
 * Extension to safely extract error messages from the Finnhub API error body.
 */
internal fun <T> Response<T>.getErrorMessage(): String {
    val errorBodyString = errorBody()?.string()
    val message = message()
    return try {
        errorBodyString?.let {
            json.decodeFromString<FinnHubAPIErrorResponse>(it).message
        } ?: message
    } catch (e: Exception) {
        if (!errorBodyString.isNullOrBlank()) errorBodyString
        else if (!message.isNullOrBlank()) message
        else e.message ?: "Unknown error"
    }
}
