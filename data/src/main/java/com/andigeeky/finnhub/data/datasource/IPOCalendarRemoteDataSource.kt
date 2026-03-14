package com.andigeeky.finnhub.data.datasource

import com.andigeeky.finnhub.data.common.NetworkResponse
import com.andigeeky.finnhub.domain.models.IPOCalendar
import java.time.LocalDate

interface IPOCalendarRemoteDataSource {
    suspend fun getIPOCalendar(
        from: LocalDate,
        to: LocalDate
    ): NetworkResponse<IPOCalendar>
}