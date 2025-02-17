package org.example.aok.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MyExposedDropdownMenuBox (
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    label: String,
    selectedOption: T?,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    getOptionDescription: (T) -> String,
    enabled: Boolean = true
) {
    val shape = if (expanded) {
        RoundedCornerShape(4.dp).copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
    } else {
        RoundedCornerShape(4.dp).copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
    }

    ExposedDropdownMenuBox(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .border(
                width = 1.dp,
                color = if (enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant,
                shape = shape
            ),
        expanded = expanded,
        onExpandedChange = { if (enabled) onExpandedChange(it) },
    ) {
        TextField(
            value = selectedOption?.let { getOptionDescription(it) } ?: "",
            enabled = enabled,
            onValueChange = { },
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
                )
            },
            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                IconButton(onClick = { if (enabled) onExpandedChange(!expanded) }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Expand",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            textStyle = MaterialTheme.typography.labelSmall,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 1f),
                disabledIndicatorColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
        if (options.isNotEmpty()) {
            ExposedDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Text(
                                text = getOptionDescription(option),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            onExpandedChange(false)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
//        else {
//            Text(
//                text = "No hay opciones disponibles",
//                modifier = Modifier.fillMaxWidth().padding(8.dp),
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.error
//            )
//        }

    }
}