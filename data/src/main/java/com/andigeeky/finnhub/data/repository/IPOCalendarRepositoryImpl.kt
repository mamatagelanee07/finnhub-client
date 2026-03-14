package com.andigeeky.finnhub.data.repository

import com.andigeeky.finnhub.data.common.networkBoundResource
import com.andigeeky.finnhub.data.datasource.IPOCalendarCacheDataSource
import com.andigeeky.finnhub.data.datasource.IPOCalendarRemoteDataSource
import com.andigeeky.finnhub.domain.common.Resource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.domain.repository.IPOCalendarRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

internal class IPOCalendarRepositoryImpl(
    private val cacheDataSource: IPOCalendarCacheDataSource,
    private val remoteDataSource: IPOCalendarRemoteDataSource
) : IPOCalendarRepository {
    override fun getIPOCalendars(
        from: LocalDate,
        to: LocalDate
    ): Flow<Resource<IPOCalendar>> {
        return networkBoundResource(
            cache = { cacheDataSource.getIPOCalendar(from, to) },
            network = { remoteDataSource.getIPOCalendar(from, to) },
            saveToCache = { cacheDataSource.saveIPOCalendar(it) },
        )
    }
}