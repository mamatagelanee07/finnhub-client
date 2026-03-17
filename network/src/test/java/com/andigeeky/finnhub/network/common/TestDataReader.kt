package com.andigeeky.finnhub.network.common

import mockwebserver3.MockResponse

object TestDataReader {
    fun mockResponse(fileName: String? = null, responseCode: Int? = null) =
        MockResponse.Builder()
            .apply { if (fileName != null) body(readFile(fileName)) }
            .apply { if (responseCode != null) code(responseCode) }
            .build()

    fun readFile(path: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(path)
            ?: throw IllegalArgumentException("File not found: $path")
        return inputStream.bufferedReader().use { it.readText() }
    }
}