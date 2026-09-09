package com.lucas.fittrack.ui.theme.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucas.fittrack.model.NutritionGoals
import com.lucas.fittrack.model.calculateDailyNutrients
import com.lucas.fittrack.model.filterMealsByDate
import com.lucas.fittrack.ui.theme.components.DateSelector
import com.lucas.fittrack.ui.theme.components.FoodDetails
import com.lucas.fittrack.ui.theme.components.FoodList
import com.lucas.fittrack.ui.theme.components.HistoryPeriodSelector
import com.lucas.fittrack.ui.theme.components.MealSummary
import com.lucas.fittrack.ui.theme.components.MealTypeSelector
import com.lucas.fittrack.ui.theme.components.NutritionGoalsEditor
import com.lucas.fittrack.ui.theme.viewmodel.HomeViewModel
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var mealTypeMenuExpanded by remember {
        mutableStateOf(false)
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

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "FitTrack"
        )

        Text(
            text = "Selecione um alimento:"
        )

        FoodList(
            foods = uiState.foods,
            onFoodSelected = { food ->
                viewModel.selectFood(food)
            }
        )

        uiState.selectedFood?.let { food ->

            FoodDetails(
                food = food,
                quantityText = uiState.quantityText,

                onQuantityChange = { newValue ->
                    viewModel.updateQuantityText(newValue)
                },

                onAdd = {
                    viewModel.addFoodToCurrentMeal()
                }
            )
        }
        Text(
            text = "Tipo da refeição:"
        )

        MealTypeSelector(
            selectedMealType = uiState.selectedMealType,
            expanded = mealTypeMenuExpanded,

            onExpandedChange = { expanded ->
                mealTypeMenuExpanded = expanded
            },

            onMealTypeSelected = { mealType ->
                viewModel.selectMealType(mealType)
            }
        )

        MealSummary(
            mealItems = uiState.mealItems
        )

        Button(
                onClick = {
                    viewModel.saveCurrentMeal()
                }
                ) {
            Text("Salvar refeição")
        }

        Text(
            text = "Refeições salvas:"
        )

        uiState.mealsOfSelectedDate.forEach { meal ->

            Text(
                text = meal.type.displayName
            )

            meal.items.forEach { item ->
                Text(
                    text = "${item.food.name} - ${item.quantityGrams} g"
                )
            }
        }

        DateSelector(
            selectedDate = uiState.selectedDate,
            onDateChange = { newDate ->
                viewModel.selectDate(newDate)
            }
        )

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

        NutritionGoalsEditor(
            caloriesText = uiState.caloriesGoalText,
            proteinText = uiState.proteinGoalText,
            carbsText = uiState.carbsGoalText,
            fatText = uiState.fatGoalText,

            onCaloriesChange = { value ->
                viewModel.updateCaloriesGoalText(value)
            },

            onProteinChange = { value ->
                viewModel.updateProteinGoalText(value)
            },

            onCarbsChange = { value ->
                viewModel.updateCarbsGoalText(value)
            },

            onFatChange = { value ->
                viewModel.updateFatGoalText(value)
            },

            onSave = {
                viewModel.saveNutritionGoals()
            },

            errorMessage = uiState.nutritionGoalsError
        )

        HistoryPeriodSelector(
            selectedPeriod = uiState.historyPeriod,
            onPeriodSelected = { period ->
                viewModel.selectHistoryPeriod(period)
            }
        )
        Text(
            text = "Histórico nutricional"
        )

        uiState.nutritionHistory.forEach { day ->

            Text(
                text = "${day.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} - " +
                        "${String.format("%.0f", day.nutrients.calories)} kcal - " +
                        "${String.format("%.1f", day.nutrients.protein)} g proteína"
            )
        }
    }


}



