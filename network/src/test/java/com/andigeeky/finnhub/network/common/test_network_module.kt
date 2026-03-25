package com.andigeeky.finnhub.network.common

import com.andigeeky.finnhub.domain.common.DomainKeys.API_KEY_PROPERTY
import com.andigeeky.finnhub.network.di.network_module
import mockwebserver3.MockWebServer
import okhttp3.HttpUrl
import org.koin.dsl.module
import org.koin.test.KoinTestRule

val test_network_module = module {
    single { MockWebServer() }
    single<HttpUrl> { get<MockWebServer>().url("/") }
}
val rule = KoinTestRule.create {
    modules(network_module + test_network_module)
    properties(mapOf(API_KEY_PROPERTY to "API Key"))
}