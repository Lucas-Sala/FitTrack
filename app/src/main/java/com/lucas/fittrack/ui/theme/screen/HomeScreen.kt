package com.lucas.fittrack.ui.theme.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucas.fittrack.model.calculateDailyNutrients
import com.lucas.fittrack.model.filterMealsByDate
import com.lucas.fittrack.ui.theme.components.DateSelector
import com.lucas.fittrack.ui.theme.components.FoodDetails
import com.lucas.fittrack.ui.theme.components.FoodList
import com.lucas.fittrack.ui.theme.components.MealSummary
import com.lucas.fittrack.ui.theme.components.MealTypeSelector
import com.lucas.fittrack.ui.theme.viewmodel.HomeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val foods by viewModel.foods.collectAsStateWithLifecycle()
    val meals by viewModel.meals.collectAsStateWithLifecycle()

    var mealTypeMenuExpanded by remember {
        mutableStateOf(false)
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
            foods = foods,
            onFoodSelected = { food ->
                viewModel.selectFood(food)
            }
        )

        viewModel.selectedFood?.let { food ->

            FoodDetails(
                food = food,
                quantityText = viewModel.quantityText,

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
            selectedMealType = viewModel.selectedMealType,
            expanded = mealTypeMenuExpanded,

            onExpandedChange = { expanded ->
                mealTypeMenuExpanded = expanded
            },

            onMealTypeSelected = { mealType ->
                viewModel.selectMealType(mealType)
            }
        )

        MealSummary(
            mealItems = viewModel.mealItems
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
        val mealsOfSelectedDate = filterMealsByDate(
            meals = meals,
            date = viewModel.selectedDate
        )

        mealsOfSelectedDate.forEach { meal ->

            Text(
                text = meal.type.displayName
            )

            meal.items.forEach { item ->
                Text(
                    text = "${item.food.name} - ${item.quantityGrams} g"
                )
            }
        }


        val dailyNutrients = calculateDailyNutrients(
            mealsOfSelectedDate
        )

        DateSelector(
            selectedDate = viewModel.selectedDate,
            onDateChange = { newDate ->
                viewModel.selectDate(newDate)
            }
        )

        Text(
            text = "Calorias: ${String.format("%.0f", dailyNutrients.calories)} kcal"
        )

        Text(
            text = "Proteínas: ${String.format("%.2f", dailyNutrients.protein)} g"
        )

        Text(
            text = "Carboidratos: ${String.format("%.2f", dailyNutrients.carbs)} g"
        )

        Text(
            text = "Gorduras: ${String.format("%.2f", dailyNutrients.fat)} g"
        )
    }
}



