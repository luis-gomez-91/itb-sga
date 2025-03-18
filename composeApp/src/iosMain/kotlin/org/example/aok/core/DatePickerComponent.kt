package org.example.aok.core

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun DatePickerComponent(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyy-MM-dd"
    }

    var date by remember { mutableStateOf(selectedDate) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = label, modifier = Modifier.padding(bottom = 8.dp))

        val uiDatePicker = remember {
            UIDatePicker().apply {
                datePickerMode = UIDatePickerMode.UIDatePickerModeDate
                preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleAutomatic

                // Objeto que maneja la actualización de la fecha
                val dateChangeHandler = object : NSObject() {
                    @ObjCAction
                    fun dateChanged() {
                        val newDate = formatter.stringFromDate(this@apply.date)
                        date = newDate
                        onDateSelected(newDate)
                    }
                }

                addTarget(
                    target = dateChangeHandler,
                    action = NSSelectorFromString("dateChanged"),
                    forControlEvents = UIControlEventValueChanged
                )
            }
        }

        UIKitView(
            factory = { uiDatePicker },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
