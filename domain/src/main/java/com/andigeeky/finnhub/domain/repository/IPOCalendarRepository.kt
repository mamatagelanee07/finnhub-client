package com.andigeeky.finnhub.domain.repository

import com.andigeeky.finnhub.domain.common.Resource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IPOCalendarRepository {
    fun getIPOCalendars(from: LocalDate, to: LocalDate): Flow<Resource<IPOCalendar>>
}