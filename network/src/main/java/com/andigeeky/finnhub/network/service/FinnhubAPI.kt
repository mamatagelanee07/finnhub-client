package com.andigeeky.finnhub.network.service

import com.andigeeky.finnhub.network.models.GetIPOCalendarResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

internal interface FinnhubAPI {
    @GET("api/v1/calendar/ipo")
    suspend fun getIPOCalendar(
        @Query("from") from: LocalDate,
        @Query("to") to: LocalDate
    ): Response<GetIPOCalendarResponse>
}