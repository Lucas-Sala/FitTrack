package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lucas.fittrack.model.HistoryPeriod
import com.lucas.fittrack.ui.theme.Dimens

@Composable
fun HistoryPeriodSelector(
    selectedPeriod: HistoryPeriod,
    onPeriodSelected: (HistoryPeriod) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            Dimens.spacingSmall
        )
    ) {
        HistoryPeriod.entries.forEach { period ->

            if (period == selectedPeriod) {
                Button(
                    onClick = {
                        onPeriodSelected(period)
                    }
                ) {
                    Text(period.displayName)
                }
            } else {
                OutlinedButton(
                    onClick = {
                        onPeriodSelected(period)
                    }
                ) {
                    Text(period.displayName)
                }
            }
        }
    }
}