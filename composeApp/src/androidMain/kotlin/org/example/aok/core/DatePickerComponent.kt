package org.example.aok.core

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.example.aok.ui.components.MyFilledTonalButton
import java.util.Calendar

@Composable
actual fun DatePickerComponent(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var date by remember { mutableStateOf(selectedDate) }
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = "$year-${month + 1}-$dayOfMonth"
            onDateSelected(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            modifier = Modifier,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.width(8.dp))
        MyFilledTonalButton(
            text = date.ifEmpty { "Seleccionar fecha" },
            enabled = true,
            buttonColor = MaterialTheme.colorScheme.surfaceContainer,
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
            icon = Icons.Filled.CalendarMonth,
            onClickAction = { datePickerDialog.show() },
            shape = RoundedCornerShape(8.dp)
        )
    }
}
