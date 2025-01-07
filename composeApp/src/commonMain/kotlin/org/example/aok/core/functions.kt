package org.example.aok.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.datetime.LocalTime
import org.example.aok.data.domain.ChipState
import org.example.aok.data.network.Response

fun String.capitalizeWords(): String {
    return this.lowercase().split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }
}

fun formatoText(titulo: String, descripcion: String): AnnotatedString {
    return buildAnnotatedString {
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(titulo)
        pop()
        append(descripcion)
    }
}

fun parseTime(hora: String): LocalTime {
    val (hour, minute) = hora.split(":").map { it.toInt() }
    return LocalTime(hour, minute)
}

@Composable
fun getChipState(documentoVerificado: Boolean): ChipState {
    return if (documentoVerificado) {
        ChipState(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            icon = Icons.Filled.Check,
            label = "Documento verificado"
        )
    } else {
        ChipState(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            labelColor = MaterialTheme.colorScheme.error,
            icon = Icons.Filled.Error,
            label = "Documento no verificado"
        )
    }
}

suspend fun requestPostDispatcher(client: HttpClient, form: Any): Response {
    return try {
        val response = client.post("${SERVER_URL}api_rest?action=postDispatcher") {
            contentType(ContentType.Application.Json)
            setBody(form)
        }
        if (response.status.isSuccess()) {
            val successResponse = response.body<Response>()
            successResponse
        } else {
            Response(status = "error", message = "Unexpected response status: ${response.status}")
        }
    } catch (e: Exception) {
        logInfo("posi", "ERROR POST DISPATCHER: ${e.message}")
        e.printStackTrace()
        Response(status = "error", message = "Exception occurred: ${e.message}")
    }
}