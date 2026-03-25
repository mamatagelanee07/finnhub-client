package com.andigeeky.finnhub.data.common

import com.andigeeky.finnhub.domain.common.FinnError.Client
import com.andigeeky.finnhub.domain.common.FinnError.Network
import com.andigeeky.finnhub.domain.common.FinnError.Server
import com.andigeeky.finnhub.domain.common.FinnError.Unknown
import com.andigeeky.finnhub.domain.common.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal inline fun <Result> networkBoundResource(
    crossinline cache: suspend () -> Flow<Result>,
    crossinline network: suspend () -> NetworkResponse<Result>,
    crossinline saveToCache: suspend (Result) -> Unit,
): Flow<Resource<Result>> = flow<Resource<Result>> {
    var latestData: Result? = null
    // 1. Immediate Loading (The Start)
    emit(Resource.Loading())

    // 2. Resilient Peek (Get Cache without crashing the whole flow)
    latestData = try {
        cache().firstOrNull()
    } catch (e: Exception) {
        e.rethrowIfCancelled()
        null
    }
    if (latestData != null) {
        emit(Resource.Success(data = latestData))
    }
    // 3. Network Sync (Runs exactly ONCE)
    try {
        when (val response = network()) {
            is NetworkResponse.Success -> saveToCache(response.response)
            is NetworkResponse.Failure -> emit(response.toErrorResource(latestData))
        }
    } catch (e: Exception) {
        e.rethrowIfCancelled()
        emit(e.toErrorResource(latestData))
    }
    // 4. Final SSOT Stream (Live Updates)
    emitAll(
        cache()
            .map { Resource.Success(data = it) }
            .catch {
                it.rethrowIfCancelled()
                it.toErrorResource(latestData)
            }
    )
}.catch {
    it.rethrowIfCancelled()
    it.toErrorResource(null)
}.flowOn(Dispatchers.IO)

/**
 * Extension to ensure cooperative cancellation in coroutines.
 */
internal fun Throwable.rethrowIfCancelled() {
    if (this is CancellationException) throw this
}

/**
 * Extension to map Network layer errors to Domain layer Resource.Error.
 */
internal fun <T> NetworkResponse.Failure<T>.toErrorResource(data: T? = null): Resource<T> {
    val error = when (this) {
        is NetworkResponse.Failure.ClientError<T> -> Client(code.value, message)
        is NetworkResponse.Failure.NoInternet<T> -> Network
        is NetworkResponse.Failure.ServerError<T> -> Server(code.value, message)
        is NetworkResponse.Failure.UnexpectedError<T> -> Unknown(message)
    }
    return Resource.Error(error, data)
}

/**
 * Extension to map Throwable to Domain layer Resource.Error.
 */
internal fun <T> Throwable.toErrorResource(data: T? = null): Resource<T> {
    return Resource.Error(Unknown(this.message), data)
}
