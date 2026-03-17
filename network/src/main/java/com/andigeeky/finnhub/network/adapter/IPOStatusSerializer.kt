package com.andigeeky.finnhub.network.adapter

import com.andigeeky.finnhub.domain.models.IPOStatus
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object IPOStatusSerializer : KSerializer<IPOStatus> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IPOStatus", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: IPOStatus) {
        value.value?.let {
            encoder.encodeString(it)
        } ?: throw IllegalArgumentException("Unknown IPO status")
    }

    override fun deserialize(decoder: Decoder): IPOStatus =
        IPOStatus.fromString(decoder.decodeString())
}