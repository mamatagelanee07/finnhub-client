package com.andigeeky.finnhub.data.usecase

import com.andigeeky.finnhub.data.common.NetworkResponse
import com.andigeeky.finnhub.data.datasource.IPOCalendarCacheDataSource
import com.andigeeky.finnhub.data.datasource.IPOCalendarRemoteDataSource
import com.andigeeky.finnhub.data.repository.IPOCalendarRepositoryImpl
import com.andigeeky.finnhub.domain.GetUpcomingIPOCalendarUseCase
import com.andigeeky.finnhub.domain.common.Resource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.domain.repository.IPOCalendarRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class GetUpcomingIPOCalendarUseCaseTest {
    private lateinit var cacheDataSource: IPOCalendarCacheDataSource
    private lateinit var remoteDataSource: IPOCalendarRemoteDataSource
    private lateinit var repository: IPOCalendarRepository
    private lateinit var useCase: GetUpcomingIPOCalendarUseCase

    private val to: LocalDate = LocalDate.now()
    private val from: LocalDate = to.minusDays(1)
    private val ipoCalendar = IPOCalendar(emptyList())


    @Before
    fun setup() {
        cacheDataSource = mockk()
        remoteDataSource = mockk()
        repository = IPOCalendarRepositoryImpl(cacheDataSource, remoteDataSource)
        useCase = GetUpcomingIPOCalendarUseCaseImpl(repository)
    }

    @Test
    fun `data is available in cache & network fetch is success`() = runTest {
        coEvery { remoteDataSource.getIPOCalendar(from, to) } returns NetworkResponse.Success(
            ipoCalendar
        )
        coEvery { cacheDataSource.getIPOCalendar(from, to) } returns flowOf(ipoCalendar)
        coEvery { cacheDataSource.saveIPOCalendar(ipoCalendar) } just Runs

        val emissions = useCase(from, to).toList()

        assertEquals(3, emissions.size)
        assertEquals(Resource.Loading<IPOCalendar>(null), emissions[0])
        assertEquals(Resource.Success(ipoCalendar, isStaleData = true), emissions[1])
        assertEquals(Resource.Success(ipoCalendar, isStaleData = false), emissions[2])

        coVerify(exactly = 1) { remoteDataSource.getIPOCalendar(from, to) }
        coVerify(exactly = 2) { cacheDataSource.getIPOCalendar(from, to) }
    }

    @Test
    fun `data fetch in cache has exception & network fetch is success`() = runTest {
        coEvery { remoteDataSource.getIPOCalendar(from, to) } returns NetworkResponse.Success(
            ipoCalendar
        )
        coEvery {
            cacheDataSource.getIPOCalendar(
                from,
                to
            )
        } throws RuntimeException() andThen flowOf(ipoCalendar)
        coEvery { cacheDataSource.saveIPOCalendar(ipoCalendar) } just Runs

        val emissions = useCase(from, to).toList()

        // Since cache fails, it skips the stale success emission
        assertEquals(2, emissions.size)
        assertEquals(Resource.Loading<IPOCalendar>(null), emissions[0])
        assertEquals(Resource.Success(ipoCalendar, isStaleData = false), emissions[1])

        coVerify(exactly = 1) { remoteDataSource.getIPOCalendar(from, to) }
        coVerify(exactly = 2) { cacheDataSource.getIPOCalendar(from, to) }
    }
}
