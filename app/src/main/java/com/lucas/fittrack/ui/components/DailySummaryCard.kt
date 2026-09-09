package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucas.fittrack.model.Nutrients
import com.lucas.fittrack.model.NutritionGoals
import com.lucas.fittrack.ui.theme.Dimens

@Composable
fun DailySummaryCard(
    nutrients: Nutrients,
    goals: NutritionGoals,
    modifier: Modifier = Modifier
) {
    val calorieProgress =
        if (goals.calories > 0) {
            (nutrients.calories / goals.calories)
                .toFloat()
                .coerceIn(0f, 1f)
        } else {
            0f
        }

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
                text = "Resumo do dia",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text =
                    "${nutrients.calories.toInt()} / " +
                            "${goals.calories.toInt()} kcal",
                style = MaterialTheme.typography.headlineSmall
            )

            LinearProgressIndicator(
                progress = { calorieProgress },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutrientSummary(
                    name = "Proteínas",
                    value = nutrients.protein,
                    goal = goals.protein
                )

                NutrientSummary(
                    name = "Carboidratos",
                    value = nutrients.carbs,
                    goal = goals.carbs
                )

                NutrientSummary(
                    name = "Gorduras",
                    value = nutrients.fat,
                    goal = goals.fat
                )
            }
        }
    }
}

@Composable
private fun NutrientSummary(
    name: String,
    value: Double,
    goal: Double
) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = "${value.toInt()} / ${goal.toInt()} g",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}