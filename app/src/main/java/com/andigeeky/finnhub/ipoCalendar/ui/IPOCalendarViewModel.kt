package com.andigeeky.finnhub.ipoCalendar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andigeeky.finnhub.common.getMessage
import com.andigeeky.finnhub.domain.GetUpcomingIPOCalendarUseCase
import com.andigeeky.finnhub.domain.common.Resource
import com.andigeeky.finnhub.domain.models.IPOCalendar
import com.andigeeky.finnhub.ipoCalendar.model.IPOCalendarState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class IPOCalendarViewModel(
    private val getIPOCalendarUseCase: GetUpcomingIPOCalendarUseCase,
) : ViewModel() {
    private val _state: StateFlow<IPOCalendarState> = getIPOCalendarUseCase(
        from = LocalDate.now().minusDays(10),
        to = LocalDate.now()
    )
        .map { map(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = IPOCalendarState.DEFAULT
        )

    val state = _state

    fun map(result: Resource<IPOCalendar>) = when (result) {
        is Resource.Error<IPOCalendar> -> IPOCalendarState(
            error = result.error.getMessage()
        )

        is Resource.Loading<IPOCalendar> -> IPOCalendarState(
            isLoading = true
        )

        is Resource.Success<IPOCalendar> -> IPOCalendarState(
            data = result.data.events
        )
    }
}