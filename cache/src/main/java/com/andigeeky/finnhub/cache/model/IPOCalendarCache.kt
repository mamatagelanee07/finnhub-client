package com.andigeeky.finnhub.cache.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.domain.models.IPOEvent
import com.andigeeky.finnhub.domain.models.IPOStatus
import com.andigeeky.finnhub.domain.models.PriceRange
import java.time.LocalDate

@Entity(tableName = "ipo_events")
data class IPOEventCache(
    val date: LocalDate?,
    val name: String?,
    @PrimaryKey val symbol: String,
    val exchange: String?,
    @Embedded(prefix = "price_") val price: PriceRange?,
    val status: IPOStatus?,
    val numberOfShares: Long?,
    val totalSharesValue: Long?
)

fun List<IPOEventCache>.toDomain() = IPOCalendar(
    this.map {
        IPOEvent(
            symbol = it.symbol,
            date = it.date,
            name = it.name,
            exchange = it.exchange,
            price = it.price,
            status = it.status,
            numberOfShares = it.numberOfShares,
            totalSharesValue = it.totalSharesValue
        )
    }
)

fun IPOEvent.toCache() = IPOEventCache(
    symbol = this.symbol ?: "",
    date = this.date,
    name = this.name,
    exchange = this.exchange,
    price = this.price,
    status = this.status,
    numberOfShares = this.numberOfShares,
    totalSharesValue = this.totalSharesValue
)
