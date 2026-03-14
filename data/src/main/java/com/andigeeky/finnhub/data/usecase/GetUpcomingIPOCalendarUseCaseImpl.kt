package com.andigeeky.finnhub.data.usecase

import com.andigeeky.finnhub.domain.GetUpcomingIPOCalendarUseCase
import com.andigeeky.finnhub.domain.repository.IPOCalendarRepository
import java.time.LocalDate

internal class GetUpcomingIPOCalendarUseCaseImpl(
    private val repository: IPOCalendarRepository
) : GetUpcomingIPOCalendarUseCase {
    override fun invoke(
        from: LocalDate,
        to: LocalDate
    ) = repository.getIPOCalendars(from, to)
}