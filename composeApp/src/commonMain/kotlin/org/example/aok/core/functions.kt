package org.example.aok.core

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import kotlinx.datetime.LocalTime

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