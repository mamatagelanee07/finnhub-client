package com.andigeeky.finnhub.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andigeeky.finnhub.cache.common.FinnHubTypeConvertors
import com.andigeeky.finnhub.cache.model.IPOEventCache

@Database(entities = [IPOEventCache::class], version = 1)
@TypeConverters(FinnHubTypeConvertors::class)
internal abstract class FinnhubDatabase : RoomDatabase() {
    abstract fun ipoCalendarDao(): IPOEventDao
}