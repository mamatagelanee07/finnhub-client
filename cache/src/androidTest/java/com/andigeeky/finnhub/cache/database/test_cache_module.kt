package com.andigeeky.finnhub.cache.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.andigeeky.finnhub.cache.di.cache_modules
import org.koin.dsl.module
import org.koin.dsl.onClose
import org.koin.test.KoinTestRule

val test_cache_module = module {
    single<Context> { ApplicationProvider.getApplicationContext() }
    single {
        Room.inMemoryDatabaseBuilder(
            get(), FinnhubDatabase::class.java
        ).allowMainThreadQueries().build()
    } onClose {
        it?.close()
    }
}
val rule = KoinTestRule.create {
    modules(cache_modules + test_cache_module)
}