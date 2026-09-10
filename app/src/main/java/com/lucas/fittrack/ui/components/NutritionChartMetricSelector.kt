package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lucas.fittrack.model.NutritionChartMetric
import com.lucas.fittrack.ui.theme.Dimens

@Composable
fun NutritionChartMetricSelector(
    selectedMetric: NutritionChartMetric,
    onMetricSelected: (NutritionChartMetric) -> Unit,
    modifier: Modifier = Modifier
) {

    val options = listOf(
        NutritionChartMetric.ALL to "Tudo",
        NutritionChartMetric.CALORIES to "Calorias",
        NutritionChartMetric.PROTEIN to "Proteínas",
        NutritionChartMetric.CARBS to "Carboidratos",
        NutritionChartMetric.FAT to "Gorduras",
        NutritionChartMetric.FIBER to "Fibras"
    )

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            Dimens.spacingSmall
        )
    ) {
        options.forEach { (metric, label) ->

            FilterChip(
                selected = selectedMetric == metric,
                onClick = {
                    onMetricSelected(metric)
                },
                label = {
                    Text(label)
                }
            )
        }
    }
}