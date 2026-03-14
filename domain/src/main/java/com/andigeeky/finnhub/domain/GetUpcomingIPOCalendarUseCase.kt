package com.andigeeky.finnhub.domain

import com.andigeeky.finnhub.domain.common.Resource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetUpcomingIPOCalendarUseCase {
    operator fun invoke(from: LocalDate, to: LocalDate): Flow<Resource<IPOCalendar>>
}