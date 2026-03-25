package com.andigeeky.finnhub.cache.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andigeeky.finnhub.cache.model.IPOEventCache
import com.andigeeky.finnhub.domain.models.IPOStatus
import com.andigeeky.finnhub.domain.models.PriceRange
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class IPOEventDaoTest : KoinTest {

    @get:Rule
    val koinTestRule = rule

    private val dao: IPOEventDao by inject()

    @Test
    fun saveAndGetIPOEvents() = runBlocking {
        val events = listOf(
            IPOEventCache(
                symbol = "AAPL",
                date = LocalDate.of(2023, 10, 27),
                name = "Apple Inc.",
                exchange = "NASDAQ",
                price = PriceRange(150.0, 160.0),
                status = IPOStatus.PRICED,
                numberOfShares = 1000000L,
                totalSharesValue = 155000000L
            ),
            IPOEventCache(
                symbol = "GOOGL",
                date = LocalDate.of(2023, 10, 28),
                name = "Alphabet Inc.",
                exchange = "NASDAQ",
                price = PriceRange(2500.0, 2600.0),
                status = IPOStatus.EXPECTED,
                numberOfShares = 500000L,
                totalSharesValue = 1275000000L
            )
        )

        dao.saveIPOEvents(events)

        val result = dao.getIPOEvents().first()
        assertEquals(events, result)
    }

    @Test
    fun upsertSameEventUpdatesData() = runBlocking {
        val event = IPOEventCache(
            symbol = "AAPL",
            date = LocalDate.of(2023, 10, 27),
            name = "Apple Inc.",
            exchange = "NASDAQ",
            price = PriceRange(150.0, 160.0),
            status = IPOStatus.EXPECTED,
            numberOfShares = 1000000L,
            totalSharesValue = 155000000L
        )
        dao.saveIPOEvents(listOf(event))

        val updatedEvent = event.copy(status = IPOStatus.PRICED)
        dao.saveIPOEvents(listOf(updatedEvent))

        val result = dao.getIPOEvents().first()
        assertEquals(1, result.size)
        assertEquals(IPOStatus.PRICED, result[0].status)
    }
}
