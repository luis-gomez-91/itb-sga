package org.example.aok.core

import androidx.compose.runtime.Composable

@Composable
expect fun DatePickerComponent(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
)