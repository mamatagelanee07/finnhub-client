package com.andigeeky.finnhub.ipoCalendar.di

import com.andigeeky.finnhub.di.di_modules
import com.andigeeky.finnhub.ipoCalendar.ui.IPOCalendarViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ipo_ui_modules = module {
    viewModel { IPOCalendarViewModel(get()) }
}

val app_di_modules = module {

} + ipo_ui_modules + di_modules