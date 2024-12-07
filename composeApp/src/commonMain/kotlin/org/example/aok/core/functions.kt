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
import kotlinx.datetime.LocalTime
import org.example.aok.data.domain.ChipState

fun String.capitalizeWords(): String {
    return this.toLowerCase().split(" ").joinToString(" ") { word ->
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