package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lucas.fittrack.model.HistoryPeriod

@Composable
fun HistoryPeriodSelector(
    selectedPeriod: HistoryPeriod,
    onPeriodSelected: (HistoryPeriod) -> Unit
) {
    Row {
        HistoryPeriod.entries.forEach { period ->

            Button(
                onClick = {
                    onPeriodSelected(period)
                }
            ) {
                Text(period.displayName)
            }
        }
    }
}