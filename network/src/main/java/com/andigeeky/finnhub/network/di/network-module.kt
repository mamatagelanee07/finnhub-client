package com.andigeeky.finnhub.network.di

import com.andigeeky.finnhub.data.datasource.IPOCalendarRemoteDataSource
import com.andigeeky.finnhub.network.common.json
import com.andigeeky.finnhub.network.datasource.IPOCalendarRemoteDataSourceImpl
import com.andigeeky.finnhub.network.intercepter.FinnhubAuthInterceptor
import com.andigeeky.finnhub.network.service.FinnhubAPI
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val ipo_calendar_network_module = module {
    factory<IPOCalendarRemoteDataSource> { IPOCalendarRemoteDataSourceImpl(get()) }
}

val network_module = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<FinnhubAuthInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(get<HttpUrl>())
            .addConverterFactory(get())
            .build()
    }
    single<FinnhubAuthInterceptor> { FinnhubAuthInterceptor(getProperty(API_KEY_PROPERTY)) }
    single<HttpUrl> { "".toHttpUrl() }
    factory<FinnhubAPI> { get<Retrofit>().create(FinnhubAPI::class.java) }
    factory<Converter.Factory> { json.asConverterFactory("application/json".toMediaType()) }
} + ipo_calendar_network_module

const val API_KEY_PROPERTY = "API_KEY_PROPERTY"