package org.itb.sga.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MySwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.secondaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
            checkedTrackColor = MaterialTheme.colorScheme.outline,
            uncheckedTrackColor = Color.Transparent,
        ),
        modifier = modifier,
        enabled = enabled
    )
}