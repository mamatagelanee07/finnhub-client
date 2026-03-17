package com.andigeeky.finnhub.network.adapter

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

internal object LocalDateSerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "LocalDate",
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: LocalDate) =
        encoder.encodeString(DateFormatter.format(value))

    override fun deserialize(decoder: Decoder): LocalDate =
        DateFormatter.parse(decoder.decodeString())
}