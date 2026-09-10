package com.lucas.fittrack.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.lucas.fittrack.model.DailyNutrition
import com.lucas.fittrack.model.NutritionGoals
import com.lucas.fittrack.ui.theme.Dimens
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NutritionChartSelectionInfo(
    day: DailyNutrition,
    nutritionGoals: NutritionGoals,
    modifier: Modifier = Modifier
) {

    val dateFormatter =
        DateTimeFormatter.ofPattern("dd/MM")

    val caloriesPercent =
        percentage(
            day.nutrients.calories,
            nutritionGoals.calories
        )

    val proteinPercent =
        percentage(
            day.nutrients.protein,
            nutritionGoals.protein
        )

    val carbsPercent =
        percentage(
            day.nutrients.carbs,
            nutritionGoals.carbs
        )

    val fatPercent =
        percentage(
            day.nutrients.fat,
            nutritionGoals.fat
        )

    val fiberPercent =
        percentage(
            day.nutrients.fiber,
            nutritionGoals.fiber
        )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                Dimens.spacingSmall
            )
    ) {

        Text(
            text = day.date.format(dateFormatter),
            style =
                MaterialTheme.typography.titleSmall
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    Dimens.spacingLarge
                ),
            verticalArrangement =
                Arrangement.spacedBy(
                    Dimens.spacingSmall
                )
        ) {

            NutritionPercentageItem(
                label = "Calorias",
                percentage = caloriesPercent,
                color = NutritionChartColors.Calories
            )

            NutritionPercentageItem(
                label = "Proteínas",
                percentage = proteinPercent,
                color = NutritionChartColors.Protein
            )

            NutritionPercentageItem(
                label = "Carboidratos",
                percentage = carbsPercent,
                color = NutritionChartColors.Carbs
            )

            NutritionPercentageItem(
                label = "Gorduras",
                percentage = fatPercent,
                color = NutritionChartColors.Fat
            )

            NutritionPercentageItem(
                label = "Fibra",
                percentage = fiberPercent,
                color = NutritionChartColors.Fiber
            )
        }
    }
}

@Composable
private fun NutritionPercentageItem(
    label: String,
    percentage: Double,
    color: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.spacedBy(
                Dimens.spacingSmall
            )
    ) {

        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )

        Text(
            text = label,
            style =
                MaterialTheme.typography.labelSmall
        )

        Text(
            text = "${percentage.toInt()}%",
            style =
                MaterialTheme.typography.labelSmall
        )
    }
}

private fun percentage(
    value: Double,
    goal: Double
): Double {

    return if (goal > 0.0) {
        value / goal * 100.0
    } else {
        0.0
    }
}