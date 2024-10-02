package org.example.aok.data.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull

//@Serializable
//data class ReportForm(
//    val reportName: String,
//    val params: List<ReportFormParams>
//)

@Serializable
data class ReportForm(
    val reportName: String,
    val params: List<Map<String, JsonElement>>
)
//
//@Serializer(forClass = Any::class)
//object AnySerializer : KSerializer<Any> {
//    override fun serialize(encoder: Encoder, value: Any) {
//        when (value) {
//            is String -> encoder.encodeString(value)
//            is Int -> encoder.encodeInt(value)
//            is Boolean -> encoder.encodeBoolean(value)
//            else -> throw IllegalArgumentException("Unsupported type: ${value::class}")
//        }
//    }
//
//    override fun deserialize(decoder: Decoder): Any {
//        val element = decoder.decodeString()
//        return element
//    }
//}