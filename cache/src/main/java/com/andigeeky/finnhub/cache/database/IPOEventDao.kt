package com.andigeeky.finnhub.cache.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.andigeeky.finnhub.cache.model.IPOEventCache
import kotlinx.coroutines.flow.Flow

@Dao
internal interface IPOEventDao {
    @Query("SELECT * FROM ipo_events")
    fun getIPOEvents(): Flow<List<IPOEventCache>>

    @Upsert
    suspend fun saveIPOEvents(events: List<IPOEventCache>)
}