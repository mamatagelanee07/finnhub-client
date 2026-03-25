package com.andigeeky.finnhub.data.datasource

import com.andigeeky.finnhub.domain.models.IPOCalendar
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IPOCalendarCacheDataSource {
    fun getIPOCalendar(
        from: LocalDate,
        to: LocalDate
    ): Flow<IPOCalendar>

    suspend fun saveIPOCalendar(
        calendar: IPOCalendar
    )
}