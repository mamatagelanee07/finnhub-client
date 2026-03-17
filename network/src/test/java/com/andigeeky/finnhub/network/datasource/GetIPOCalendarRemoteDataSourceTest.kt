package com.andigeeky.finnhub.network.datasource

import com.andigeeky.finnhub.data.common.NetworkResponse
import com.andigeeky.finnhub.data.datasource.IPOCalendarRemoteDataSource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.domain.models.IPOEvent
import com.andigeeky.finnhub.domain.models.IPOStatus
import com.andigeeky.finnhub.domain.models.PriceRange
import com.andigeeky.finnhub.network.common.TestDataReader
import com.andigeeky.finnhub.network.common.rule
import com.andigeeky.finnhub.network.intercepter.FinnhubAuthInterceptor.Companion.AUTH_TOKEN_HEADER_KEY
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import java.time.LocalDate


class GetIPOCalendarRemoteDataSourceTest : KoinTest {
    @get:Rule
    val koinTestRule = rule
    private val server: MockWebServer by inject()
    private val dataSource: IPOCalendarRemoteDataSource by inject()

    private val calendar = IPOCalendar(
        events = (0..1).map {
            IPOEvent(
                symbol = "Symbol$it",
                exchange = "Exchange$it",
                price = PriceRange(100.0 * it, 200.0 * it),
                date = LocalDate.of(2026, 2 * it + 1, 7 * it + 1),
                numberOfShares = 1000L * it,
                name = "Name$it",
                status = IPOStatus.entries[it],
                totalSharesValue = 200000L * it + 100000,
            )
        }
    )

    @Before
    fun setup() {
        server.start()
    }

    @After
    fun tearDown() {
        server.close()
    }

    @Test
    fun `getIPOCalendar returns success when server returns 200`() = runTest {
        // Arrange
        server.enqueue(
            TestDataReader.mockResponse(
                "ipo-calendar-success.json",
                200
            )
        )

        val from = LocalDate.of(2025, 2, 2)
        val to = LocalDate.of(2025, 2, 10)

        // Act
        val result = dataSource.getIPOCalendar(from, to)

        // Assert
        val request = server.takeRequest()
        assertEquals("/calendar/ipo", request.url.encodedPath)
        assertEquals("2025-02-02", request.url.queryParameter("from"))
        assertEquals("2025-02-10", request.url.queryParameter("to"))
        assertEquals("API Key", request.headers[AUTH_TOKEN_HEADER_KEY])
        assertTrue(result is NetworkResponse.Success)
        assertEquals(calendar.events, (result as NetworkResponse.Success).response.events)
    }

    @Test
    fun `getIPOCalendar returns error when server returns 500`() = runTest {
        // Arrange
        server.enqueue(
            TestDataReader.mockResponse(
                "ipo-calendar-error.json",
                500
            )
        )

        val from = LocalDate.of(2025, 2, 2)
        val to = LocalDate.of(2025, 2, 10)

        // Act
        val result = dataSource.getIPOCalendar(from, to)

        // Assert
        assertTrue(result is NetworkResponse.Failure.ServerError)
        val response = result as NetworkResponse.Failure.ServerError
        assertEquals("this is an error from server for x reason", response.message)
        assertEquals(500, response.code.value)
    }

    @Test
    fun `getIPOCalendar returns error when server returns 404`() = runTest {
        // Arrange
        server.enqueue(
            TestDataReader.mockResponse(
                "ipo-calendar-error.json",
                404
            )
        )

        val from = LocalDate.of(2025, 2, 2)
        val to = LocalDate.of(2025, 2, 10)

        // Act
        val result = dataSource.getIPOCalendar(from, to)

        // Assert
        assertTrue(result is NetworkResponse.Failure.ClientError)
        val response = result as NetworkResponse.Failure.ClientError
        assertEquals("this is an error from server for x reason", response.message)
        assertEquals(404, response.code.value)
    }
}
