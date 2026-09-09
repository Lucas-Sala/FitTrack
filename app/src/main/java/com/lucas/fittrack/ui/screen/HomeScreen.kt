package com.lucas.fittrack.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucas.fittrack.ui.components.DateSelector
import com.lucas.fittrack.ui.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("FitTrack")

        DateSelector(
            selectedDate = uiState.selectedDate,
            onDateChange = { newDate ->
                viewModel.selectDate(newDate)
            }
        )

        Text("Resumo do dia")

        NutrientProgress(
            name = "Calorias",
            consumed = uiState.dailyNutrients.calories,
            goal = uiState.nutritionGoals.calories,
            unit = "kcal"
        )

        NutrientProgress(
            name = "Proteínas",
            consumed = uiState.dailyNutrients.protein,
            goal = uiState.nutritionGoals.protein,
            unit = "g"
        )

        NutrientProgress(
            name = "Carboidratos",
            consumed = uiState.dailyNutrients.carbs,
            goal = uiState.nutritionGoals.carbs,
            unit = "g"
        )

        NutrientProgress(
            name = "Gorduras",
            consumed = uiState.dailyNutrients.fat,
            goal = uiState.nutritionGoals.fat,
            unit = "g"
        )

        Text("Refeições do dia")

        uiState.mealsOfSelectedDate.forEach { meal ->
            Text(meal.type.displayName)

            meal.items.forEach { item ->
                Text(
                    "${item.food.name} - ${item.quantityGrams} g"
                )
            }
        }
    }
}

fun calculateProgress(
    consumed: Double,
    goal: Double
): Float {
    if (goal <= 0.0) {
        return 0f
    }

    return (consumed / goal)
        .toFloat()
        .coerceIn(0f, 1f)
}

@Composable
fun NutrientProgress(
    name: String,
    consumed: Double,
    goal: Double,
    unit: String
) {
    Text(
        text = "$name: %.1f / %.1f $unit".format(
            consumed,
            goal
        )
    )

    LinearProgressIndicator(
        progress = {
            calculateProgress(
                consumed = consumed,
                goal = goal
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}