package com.lucas.fittrack.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucas.fittrack.ui.components.DailySummaryCard
import com.lucas.fittrack.ui.components.DateSelector
import com.lucas.fittrack.ui.components.MealCard
import com.lucas.fittrack.ui.components.SectionCard
import com.lucas.fittrack.ui.theme.Dimens
import com.lucas.fittrack.ui.viewmodel.HomeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(
            Dimens.spacingLarge
        )
    ) {
        Text(
            text = "FitTrack",
            style = MaterialTheme.typography.headlineLarge
        )

        DateSelector(
            selectedDate = uiState.selectedDate,
            onDateChange = viewModel::selectDate
        )

        DailySummaryCard(
            nutrients = uiState.dailyNutrients,
            goals = uiState.nutritionGoals
        )

        Text(
            text = "Refeições do dia",
            style = MaterialTheme.typography.titleLarge
        )
        SectionCard {

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
            )

            if (uiState.mealsOfSelectedDate.isEmpty()) {

                Text(
                    text = "Nenhuma Refeição",
                    style = MaterialTheme.typography.bodyMedium
                )

            } else {

                uiState.mealsOfSelectedDate.forEach { meal ->
                    MealCard(
                        meal = meal
                    )
                }
            }
        }
    }
}

//fun calculateProgress(
//    consumed: Double,
//    goal: Double
//): Float {
//    if (goal <= 0.0) {
//        return 0f
//    }
//
//    return (consumed / goal)
//        .toFloat()
//        .coerceIn(0f, 1f)
//}
//
//@Composable
//fun NutrientProgress(
//    name: String,
//    consumed: Double,
//    goal: Double,
//    unit: String
//) {
//    Text(
//        text = "$name: %.1f / %.1f $unit".format(
//            consumed,
//            goal
//        )
//    )
//
//    LinearProgressIndicator(
//        progress = {
//            calculateProgress(
//                consumed = consumed,
//                goal = goal
//            )
//        },
//        modifier = Modifier.fillMaxWidth()
//    )
//}