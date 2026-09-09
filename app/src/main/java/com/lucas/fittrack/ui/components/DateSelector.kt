package com.lucas.fittrack.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.lucas.fittrack.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern(
        "dd 'de' MMMM 'de' yyyy",
        Locale("pt", "BR")
    )

    val today = LocalDate.now()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                onClick = {
                    onDateChange(
                        selectedDate.minusDays(1)
                    )
                }
            ) {
                Icon(
                    painter = painterResource(
                        R.drawable.ic_chevron_left
                    ),
                    contentDescription = "Dia anterior"
                )
            }

            Text(
                text = selectedDate.format(formatter),
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = {
                    onDateChange(
                        selectedDate.plusDays(1)
                    )
                }
            ) {
                Icon(
                    painter = painterResource(
                        R.drawable.ic_chevron_right
                    ),
                    contentDescription = "Próximo dia"
                )
            }
        }

        val dayOfWeekFormatter = DateTimeFormatter.ofPattern(
            "EEEE",
            Locale("pt", "BR")
        )

        val dateDescription = when (selectedDate) {
            today -> "Hoje"
            today.minusDays(1) -> "Ontem"
            today.plusDays(1) -> "Amanhã"
            else -> selectedDate
                .format(dayOfWeekFormatter)
                .replaceFirstChar { it.uppercase() }
        }

        Text(
            text = dateDescription,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}