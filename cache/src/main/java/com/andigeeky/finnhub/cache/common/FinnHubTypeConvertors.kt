package com.andigeeky.finnhub.cache.common

import androidx.room.TypeConverter
import com.andigeeky.finnhub.domain.models.IPOStatus
import java.time.LocalDate

class FinnHubTypeConvertors {
    @TypeConverter
    fun fromLocalDateToString(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun fromStringToLocalDate(date: String?): LocalDate? = date?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromStatus(status: IPOStatus?): String? = status?.name

    @TypeConverter
    fun toStatus(value: String?): IPOStatus? = value?.let {
        try {
            IPOStatus.valueOf(it)
        } catch (e: Exception) {
            null
        }
    }
}
