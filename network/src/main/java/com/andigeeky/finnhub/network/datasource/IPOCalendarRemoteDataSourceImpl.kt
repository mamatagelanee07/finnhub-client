package com.andigeeky.finnhub.network.datasource

import com.andigeeky.finnhub.data.common.NetworkResponse
import com.andigeeky.finnhub.data.datasource.IPOCalendarRemoteDataSource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.network.common.networkCall
import com.andigeeky.finnhub.network.models.toDomain
import com.andigeeky.finnhub.network.service.FinnhubAPI
import java.time.LocalDate

internal class IPOCalendarRemoteDataSourceImpl(
    val api: FinnhubAPI
) : IPOCalendarRemoteDataSource {
    override suspend fun getIPOCalendar(
        from: LocalDate,
        to: LocalDate
    ): NetworkResponse<IPOCalendar> = networkCall(
        network = { api.getIPOCalendar(from, to) },
        map = { it.toDomain() }
    )
}