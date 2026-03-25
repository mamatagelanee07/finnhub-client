package com.andigeeky.finnhub.ipoCalendar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andigeeky.finnhub.ipoCalendar.model.IPOCalendarState
import org.koin.androidx.compose.koinViewModel

@Composable
fun IPOCalendarScreen(
    viewModel: IPOCalendarViewModel = koinViewModel()
) {
    IPOCalendarComponent(
        state = viewModel.state.collectAsStateWithLifecycle().value
    )
}

@Composable
fun IPOCalendarComponent(
    state: IPOCalendarState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading)
            CircularProgressIndicator()
        state.error?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = it
            )
        }
        state.data?.let {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(it) { item ->
                    Text(
                        text = item.symbol.orEmpty()
                    )
                }
            }
        }
    }
}