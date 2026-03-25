package com.andigeeky.finnhub.ipoCalendar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andigeeky.finnhub.common.getMessage
import com.andigeeky.finnhub.domain.GetUpcomingIPOCalendarUseCase
import com.andigeeky.finnhub.domain.common.Resource
import com.andigeeky.finnhub.ipoCalendar.model.IPOCalendarState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class IPOCalendarViewModel(
    private val getIPOCalendarUseCase: GetUpcomingIPOCalendarUseCase,
) : ViewModel() {

    private val reloadTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val state: StateFlow<IPOCalendarState> = reloadTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            getIPOCalendarUseCase(
                from = LocalDate.now().minusDays(10),
                to = LocalDate.now()
            )
        }
        .scan(IPOCalendarState.DEFAULT) { previousState, result ->
            when (result) {
                is Resource.Loading -> previousState.copy(
                    isLoading = true,
                    error = null
                )

                is Resource.Success -> IPOCalendarState(
                    data = result.data.events.toImmutableList(),
                    isLoading = false,
                    error = null
                )

                is Resource.Error -> previousState.copy(
                    isLoading = false,
                    error = result.error.getMessage(),
                    data = result.data?.events?.toImmutableList() ?: previousState.data
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = IPOCalendarState.DEFAULT
        )

    fun reload() {
        reloadTrigger.tryEmit(Unit)
    }
}
