package com.andigeeky.finnhub.di

import com.andigeeky.finnhub.cache.di.cache_modules
import com.andigeeky.finnhub.data.di.data_modules
import com.andigeeky.finnhub.network.di.network_module

val di_modules = data_modules + network_module + cache_modules