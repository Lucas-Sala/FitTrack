package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucas.fittrack.model.DailyNutrition
import com.lucas.fittrack.ui.theme.Dimens

@Composable
fun PeriodSummaryCard(
    history: List<DailyNutrition>,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) {
        return
    }

    val averageCalories =
        history.map { it.nutrients.calories }.average()

    val averageProtein =
        history.map { it.nutrients.protein }.average()

    val averageCarbs =
        history.map { it.nutrients.carbs }.average()

    val averageFat =
        history.map { it.nutrients.fat }.average()

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(Dimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(
                Dimens.spacingMedium
            )
        ) {

            Text(
                text = "Resumo do período",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Média diária",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "${averageCalories.toInt()} kcal",
                style = MaterialTheme.typography.headlineSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                PeriodNutrientSummary(
                    name = "Proteínas",
                    value = averageProtein
                )

                PeriodNutrientSummary(
                    name = "Carboidratos",
                    value = averageCarbs
                )

                PeriodNutrientSummary(
                    name = "Gorduras",
                    value = averageFat
                )
            }
        }
    }
}

@Composable
private fun PeriodNutrientSummary(
    name: String,
    value: Double
) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = "${value.toInt()} g",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}