package com.andigeeky.finnhub.ipoCalendar.model

import com.andigeeky.finnhub.domain.models.IPOEvent

data class IPOCalendarState(
    val isLoading: Boolean = false,
    val error : String? = null,
    val data : List<IPOEvent>? = null
){
    companion object {
        val DEFAULT = IPOCalendarState()
    }
}