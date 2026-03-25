package com.andigeeky.finnhub.cache.di

import androidx.room.Room
import com.andigeeky.finnhub.cache.database.FinnhubDatabase
import com.andigeeky.finnhub.cache.datasource.IPOCalendarCacheDataSourceImpl
import com.andigeeky.finnhub.data.datasource.IPOCalendarCacheDataSource
import org.koin.dsl.module

internal val ipo_calendar_module = module {
    factory { get<FinnhubDatabase>().ipoCalendarDao() }
    single<IPOCalendarCacheDataSource> { IPOCalendarCacheDataSourceImpl(get()) }
}
val cache_modules = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = FinnhubDatabase::class.java,
            name = "finnhub_database"
        ).build()
    }
} + ipo_calendar_module
