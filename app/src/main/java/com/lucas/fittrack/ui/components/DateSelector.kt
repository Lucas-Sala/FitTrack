package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Row {
        Button(
            onClick = {
                onDateChange(selectedDate.minusDays(1))
            }
        ) {
            Text("<")
        }

        Text(
            text = selectedDate.format(formatter)
        )

        Button(
            onClick = {
                onDateChange(selectedDate.plusDays(1))
            }
        ) {
            Text(">")
        }
    }
}