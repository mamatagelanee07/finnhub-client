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
    ) = encoder.encodeString("${value.min}-${value.max}")

    override fun deserialize(decoder: Decoder): PriceRange {
        val parts = decoder.decodeString().split("-")
        if (parts.size == 2) {
            return PriceRange(
                min = parts[0].trim().toDouble(),
                max = parts[1].trim().toDouble()
            )
        } else {
            throw IllegalArgumentException("Invalid price range format")
        }
    }
}