@file:OptIn(ExperimentalSerializationApi::class)

package com.andigeeky.finnhub.network.models

import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.domain.models.IPOEvent
import com.andigeeky.finnhub.domain.models.IPOStatus
import com.andigeeky.finnhub.domain.models.PriceRange
import com.andigeeky.finnhub.network.adapter.IPOStatusSerializer
import com.andigeeky.finnhub.network.adapter.LocalDateSerializer
import com.andigeeky.finnhub.network.adapter.PriceRangeSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.time.LocalDate

@Serializable
internal data class GetIPOCalendarResponse(
    @JsonNames("ipoCalendar") val calendars: List<IPOCalendarRemote>
)

@Serializable
internal data class IPOCalendarRemote(
    @Serializable(with = LocalDateSerializer::class)
    @JsonNames("date")
    val date: LocalDate?,
    @JsonNames("exchange")
    val exchange: String,
    @JsonNames("name")
    val name: String,
    @JsonNames("numberOfShares")
    val numberOfShares: Long,
    @Serializable(with = PriceRangeSerializer::class)
    @JsonNames("price")
    val price: PriceRange,
    @Serializable(IPOStatusSerializer::class)
    @JsonNames("status")
    val status: IPOStatus,
    @JsonNames("symbol")
    val symbol: String,
    @JsonNames("totalSharesValue")
    val totalSharesValue: Long,
)

internal fun GetIPOCalendarResponse.toDomain() = IPOCalendar(
    events = calendars.map {
        IPOEvent(
            date = it.date,
            exchange = it.exchange,
            name = it.name,
            numberOfShares = it.numberOfShares,
            price = it.price,
            status = it.status,
            symbol = it.symbol,
            totalSharesValue = it.totalSharesValue
        )
    }
)