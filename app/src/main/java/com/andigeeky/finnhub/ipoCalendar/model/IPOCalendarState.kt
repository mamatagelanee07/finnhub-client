package com.andigeeky.finnhub.ipoCalendar.model

import androidx.compose.runtime.Immutable
import com.andigeeky.finnhub.domain.models.IPOEvent
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class IPOCalendarState(
    val isLoading: Boolean = false,
    val error : String? = null,
    val data: ImmutableList<IPOEvent>? = null
){
    companion object {
        val DEFAULT = IPOCalendarState()
    }
}
