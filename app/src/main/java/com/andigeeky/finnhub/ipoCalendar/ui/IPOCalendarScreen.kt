package com.andigeeky.finnhub.ipoCalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andigeeky.finnhub.domain.models.IPOEvent
import com.andigeeky.finnhub.domain.models.IPOStatus
import com.andigeeky.finnhub.domain.models.PriceRange
import com.andigeeky.finnhub.ipoCalendar.model.IPOCalendarState
import com.andigeeky.finnhub.ui.theme.FinnhubclientTheme
import com.skydoves.compose.stability.runtime.TraceRecomposition
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun IPOCalendarScreen(
    viewModel: IPOCalendarViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val reload = remember(viewModel) {
        { viewModel.reload() }
    }
    IPOCalendarComponent(
        state = state,
        reload = reload
    )
}

@Composable
fun IPOCalendarComponent(
    state: IPOCalendarState,
    reload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = reload
        ) {
            Text("Reload")
        }
        if (state.isLoading)
            CircularProgressIndicator()
        state.error?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = it
            )
        }
        state.data?.let {
            IPOEventList(it)
        }
    }
}

@TraceRecomposition
@Composable
private fun IPOEventList(events: ImmutableList<IPOEvent>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(events) { item ->
            Text(
                text = item.symbol.orEmpty(),
                color = Color.Black,
            )
        }
    }
}

class IPOCalendarComponentPreviewProvider : PreviewParameterProvider<IPOCalendarState> {
    override val values = sequenceOf(
        IPOCalendarState(isLoading = true),
        IPOCalendarState(error = "there is an error"),
        IPOCalendarState(
            data = (1..10).map { i ->
                IPOEvent(
                    date = LocalDate.now().plusDays(i.toLong()),
                    name = "Company $i Inc",
                    symbol = "COMP$i",
                    exchange = if (i % 2 == 0) "NASDAQ" else "NYSE",
                    price = PriceRange(10.0 + i, 15.0 + i),
                    status = IPOStatus.EXPECTED,
                    numberOfShares = 1000000L * i,
                    totalSharesValue = 15000000L * i
                )
            }.toImmutableList()
        )
    )
}

@Preview
@Composable
fun IPOCalendarComponentPreview(
    @PreviewParameter(IPOCalendarComponentPreviewProvider::class) state: IPOCalendarState
) {
    FinnhubclientTheme(darkTheme = false) {
        IPOCalendarComponent(state, {})
    }
}
