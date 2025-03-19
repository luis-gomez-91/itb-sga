package org.itb.sga.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyAssistChip(
    label: String,
    containerColor: Color,
    labelColor: Color,
    icon: ImageVector? = null
) {
    Box(
        modifier = Modifier.height(24.dp), // Ajusta la altura del chip
        contentAlignment = Alignment.Center
    ) {
        AssistChip(
            modifier = Modifier.padding(0.dp),
            onClick = { },
            label = { Text(text = label, fontSize = 10.sp) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = containerColor,
                labelColor = labelColor
            ),
            border = null,
            leadingIcon = {
                icon?.let {
                    Icon(
                        icon,
                        contentDescription = "Icon",
                        modifier = Modifier.size(AssistChipDefaults.IconSize),
                        tint = labelColor
                    )
                }
            },
        )
    }
}