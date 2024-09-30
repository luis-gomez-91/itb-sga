package org.example.aok.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOutlinedTextField(
    value: String,
    placeholder: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.background(color = MaterialTheme.colorScheme.background),
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    )
}