package com.andigeeky.finnhub.data.di

import com.andigeeky.finnhub.data.repository.IPOCalendarRepositoryImpl
import com.andigeeky.finnhub.data.usecase.GetUpcomingIPOCalendarUseCaseImpl
import com.andigeeky.finnhub.domain.GetUpcomingIPOCalendarUseCase
import com.andigeeky.finnhub.domain.repository.IPOCalendarRepository
import org.koin.dsl.module

internal val ipo_data_module = module {
    factory<GetUpcomingIPOCalendarUseCase> { GetUpcomingIPOCalendarUseCaseImpl(get()) }
    factory<IPOCalendarRepository> { IPOCalendarRepositoryImpl(get(), get()) }
}
val data_modules = module {

} + ipo_data_module