package org.example.aok.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
//        placeholder = { Text(text = placeholder) },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
            )
        },
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        ),
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
//            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
//            disabledBorderColor = MaterialTheme.colorScheme.outlineVariant
//        ),
        enabled = enabled
    )
}