package com.andigeeky.finnhub.network.adapter

import com.andigeeky.finnhub.domain.models.PriceRange
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class PriceRangeSerializer : KSerializer<PriceRange> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(
            serialName = "PriceRange",
            kind = PrimitiveKind.STRING,
        )

    override fun serialize(
        encoder: Encoder,
        value: PriceRange
    ) {
        val output = when (value.min) {
            null if value.max == null -> ""
            value.max -> "${value.min}"
            else -> "${value.min ?: ""}-${value.max ?: ""}"
        }
        encoder.encodeString(output)
    }

    override fun deserialize(decoder: Decoder): PriceRange {
        val input = decoder.decodeString().trim()
        if (input.isBlank() || input == "null") return PriceRange(null, null)

        // Handle both "-" and " - " as separators
        val parts = input.split("-").map { it.trim() }.filter { it.isNotEmpty() }
        
        return when (parts.size) {
            1 -> {
                val price = parts[0].toDoubleOrNull()
                PriceRange(min = price, max = price)
            }
            2 -> {
                PriceRange(
                    min = parts[0].toDoubleOrNull(),
                    max = parts[1].toDoubleOrNull()
                )
            }
            else -> PriceRange(null, null)
        }
    }
}
