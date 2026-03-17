package com.andigeeky.finnhub.network.adapter

import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal object DateFormatter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd

    fun format(date: LocalDate): String = date.format(formatter)
    fun parse(date: String): LocalDate = LocalDate.parse(date, formatter)
}