package com.andigeeky.finnhub

import android.app.Application
import com.andigeeky.finnhub.domain.common.DomainKeys.API_KEY_PROPERTY
import com.andigeeky.finnhub.ipoCalendar.di.app_di_modules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FinnhubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FinnhubApplication)
            androidLogger(Level.ERROR)
            modules(app_di_modules)
            properties(mapOf(API_KEY_PROPERTY to BuildConfig.FINNHUB_API_KEY))
        }
    }
}