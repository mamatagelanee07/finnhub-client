package com.andigeeky.finnhub.cache.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andigeeky.finnhub.cache.database.rule
import com.andigeeky.finnhub.data.datasource.IPOCalendarCacheDataSource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.domain.models.IPOEvent
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
class IPOCalendarCacheDataSourceImplTest : KoinTest {

    @get:Rule
    val koinTestRule = rule

    private val dataSource: IPOCalendarCacheDataSource by inject()

    @Test
    fun saveAndGetIPOCalendar() = runBlocking {
        val calendar = IPOCalendar(
            events = listOf(
                IPOEvent(
                    symbol = "AAPL",
                    date = LocalDate.of(2023, 10, 27),
                    name = "Apple Inc.",
                    exchange = "NASDAQ",
                    price = PriceRange(150.0, 160.0),
                    status = IPOStatus.PRICED,
                    numberOfShares = 1000000L,
                    totalSharesValue = 155000000L
                )
            )
        )

        dataSource.saveIPOCalendar(calendar)

        val result = dataSource.getIPOCalendar(
            from = LocalDate.of(2023, 10, 1),
            to = LocalDate.of(2023, 10, 31)
        ).first()

        assertEquals(calendar, result)
    }
}
