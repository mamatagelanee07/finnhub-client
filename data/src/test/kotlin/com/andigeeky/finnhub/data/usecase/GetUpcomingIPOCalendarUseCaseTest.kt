package com.andigeeky.finnhub.data.usecase

import app.cash.turbine.test
import com.andigeeky.finnhub.data.common.NetworkResponse
import com.andigeeky.finnhub.data.datasource.IPOCalendarCacheDataSource
import com.andigeeky.finnhub.data.datasource.IPOCalendarRemoteDataSource
import com.andigeeky.finnhub.data.repository.IPOCalendarRepositoryImpl
import com.andigeeky.finnhub.domain.GetUpcomingIPOCalendarUseCase
import com.andigeeky.finnhub.domain.common.FinnError
import com.andigeeky.finnhub.domain.common.Resource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.domain.repository.IPOCalendarRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
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

    private val to: LocalDate = LocalDate.of(2026, 2, 1)
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

        useCase(from, to).test {
            // Initial Loading
            assertEquals(Resource.Loading<IPOCalendar>(), awaitItem())
            // Data from Cache
            assertEquals(Resource.Success(ipoCalendar), awaitItem())
            // Data from network
            assertEquals(Resource.Success(ipoCalendar), awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { remoteDataSource.getIPOCalendar(from, to) }
        coVerify(exactly = 2) { cacheDataSource.getIPOCalendar(from, to) }
    }

    @Test
    fun `data is available in cache & network fetch fails`() = runTest {
        coEvery { cacheDataSource.getIPOCalendar(from, to) } returns flowOf(ipoCalendar)
        coEvery { cacheDataSource.saveIPOCalendar(ipoCalendar) } just Runs
        coEvery {
            remoteDataSource.getIPOCalendar(
                from,
                to
            )
        } returns NetworkResponse.Failure.UnexpectedError(
            message = "Network Error"
        )

        useCase(from, to).test {
            // Initial Loading
            assertEquals(Resource.Loading<IPOCalendar>(), awaitItem())
            // Data from Cache
            assertEquals(Resource.Success(ipoCalendar), awaitItem())
            // Data from network
            assertEquals(
                Resource.Error(
                    data = ipoCalendar,
                    error = FinnError.Unknown("Network Error")
                ), awaitItem()
            )
            assertEquals(Resource.Success(ipoCalendar), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `cache is empty & network fetch succeeds`() = runTest {
        coEvery { cacheDataSource.getIPOCalendar(from, to) } returnsMany listOf(
            emptyFlow(),
            flowOf(ipoCalendar)
        )
        coEvery { cacheDataSource.saveIPOCalendar(ipoCalendar) } just Runs
        coEvery { remoteDataSource.getIPOCalendar(from, to) } returns NetworkResponse.Success(
            ipoCalendar
        )

        useCase(from, to).test {
            // Initial Loading
            assertEquals(Resource.Loading<IPOCalendar>(), awaitItem())
            // Data from Cache after network call
            assertEquals(Resource.Success(ipoCalendar), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `cache is empty & network fetch fails`() = runTest {
        coEvery { cacheDataSource.getIPOCalendar(from, to) } returns emptyFlow()
        coEvery { cacheDataSource.saveIPOCalendar(ipoCalendar) } just Runs
        coEvery {
            remoteDataSource.getIPOCalendar(
                from,
                to
            )
        } returns NetworkResponse.Failure.UnexpectedError(
            message = "Network Error"
        )

        useCase(from, to).test {
            // Initial Loading
            assertEquals(Resource.Loading<IPOCalendar>(), awaitItem())
            // Data from Cache after network call
            assertEquals(
                Resource.Error<IPOCalendar>(error = FinnError.Unknown("Network Error")),
                awaitItem()
            )
            awaitComplete()
        }
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

        useCase(from, to).test {
            assertEquals(Resource.Loading<IPOCalendar>(), awaitItem())
            assertEquals(Resource.Success(ipoCalendar), awaitItem())
            awaitComplete()
        }
        coVerify(exactly = 1) { remoteDataSource.getIPOCalendar(from, to) }
        coVerify(exactly = 2) { cacheDataSource.getIPOCalendar(from, to) }
    }
}
