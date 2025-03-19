package org.itb.sga.data.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ReportForm(
    val reportName: String,
    val params: Map<String, JsonElement>
)