package com.andigeeky.finnhub.domain.models

import java.time.LocalDate

data class IPOCalendar(
    val events: List<IPOEvent>
)

data class IPOEvent(
    val date: LocalDate?,
    val name: String?,
    val symbol: String?,
    val exchange: String?,
    val price: PriceRange?,
    val status: IPOStatus?,
    val numberOfShares: Long?,
    val totalSharesValue: Long?
)

data class PriceRange(
    val min: Double?,
    val max: Double?
)

enum class IPOStatus(val value: String?) {
    EXPECTED("expected"),
    PRICED("priced"),
    WITHDRAWN("withdrawn"),
    FILED("filed"),
    UNKNOWN(null);

    companion object {
        fun fromString(value: String?): IPOStatus {
            return entries.find { it.value == value } ?: UNKNOWN
        }
    }
}
