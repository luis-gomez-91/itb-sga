package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
sealed class ValueData {
    @Serializable
    data class StringValue(val stringValue: String) : ValueData()

    @Serializable
    data class IntValue(val intValue: Int) : ValueData()

    @Serializable
    data class BooleanValue(val boolValue: Boolean) : ValueData()
}

@Serializable
data class UpdateValue(
    val action: String,
    val id: Int,
    val value: ValueData
)