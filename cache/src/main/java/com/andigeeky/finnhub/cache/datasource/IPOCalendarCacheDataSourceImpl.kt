package com.andigeeky.finnhub.cache.datasource

import com.andigeeky.finnhub.cache.database.IPOEventDao
import com.andigeeky.finnhub.cache.model.toCache
import com.andigeeky.finnhub.cache.model.toDomain
import com.andigeeky.finnhub.data.datasource.IPOCalendarCacheDataSource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import kotlinx.coroutines.flow.map
import java.time.LocalDate

internal class IPOCalendarCacheDataSourceImpl(
    private val dao: IPOEventDao
) : IPOCalendarCacheDataSource {
    override fun getIPOCalendar(
        from: LocalDate,
        to: LocalDate
    ) = dao.getIPOEvents().map {
        it.toDomain()
    }

    override suspend fun saveIPOCalendar(calendar: IPOCalendar) {
        dao.saveIPOEvents(calendar.events.map { it.toCache() })
    }
}