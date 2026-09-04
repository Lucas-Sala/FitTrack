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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucas.fittrack.model.Food
import com.lucas.fittrack.model.Meal
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.MealType
import com.lucas.fittrack.model.calculateDailyNutrients
import com.lucas.fittrack.model.filterMealsByDate
import com.lucas.fittrack.model.sampleFoods
import com.lucas.fittrack.ui.theme.components.DateSelector
import com.lucas.fittrack.ui.theme.components.FoodDetails
import com.lucas.fittrack.ui.theme.components.FoodList
import com.lucas.fittrack.ui.theme.components.MealSummary
import com.lucas.fittrack.ui.theme.components.MealTypeSelector
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var selectedMealType by remember {
        mutableStateOf(MealType.LUNCH)
    }
    var mealTypeMenuExpanded by remember {
        mutableStateOf(false)
    }

    val mealItems = remember {
        mutableStateListOf<MealItem>()
    }
    val meals = remember {
        mutableStateListOf<Meal>()
    }

    var selectedFood by remember {
        mutableStateOf<Food?>(null)
    }

    var quantityText by remember {
        mutableStateOf("")
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
            foods = sampleFoods,
            onFoodSelected = { food ->
                selectedFood = food
            }
        )

        selectedFood?.let { food ->

            FoodDetails(
                food = food,
                quantityText = quantityText,

                onQuantityChange = { newValue ->
                    quantityText = newValue
                },

                onAdd = {
                    val quantity = quantityText.toDoubleOrNull() ?: 0.0

                    if (quantity > 0.0) {
                        mealItems.add(
                            MealItem(
                                food = food,
                                quantityGrams = quantity
                            )
                        )
                    }
                }
            )
        }
        Text(
            text = "Tipo da refeição:"
        )

        MealTypeSelector(
            selectedMealType = selectedMealType,
            expanded = mealTypeMenuExpanded,

            onExpandedChange = { expanded ->
                mealTypeMenuExpanded = expanded
            },

            onMealTypeSelected = { mealType ->
                selectedMealType = mealType
            }
        )

        MealSummary(
            mealItems = mealItems
        )

        Button(
            onClick = {
                if (mealItems.isNotEmpty()) {
                    val currentTime = LocalDateTime.now().toLocalTime()

                    val meal = Meal(
                        type = selectedMealType,
                        dateTime = selectedDate.atTime(currentTime),
                        items = mealItems.toList()
                    )
//                    val meal = Meal(
//                        type = selectedMealType,
//                        dateTime = LocalDateTime.now(),
//                        items = mealItems.toList()
//                    )

                    meals.add(meal)

                    mealItems.clear()
                }
            }
        ) {
            Text("Salvar refeição")
        }

        Text(
            text = "Refeições salvas:"
        )

        meals.forEach { meal ->

            Text(
                text = meal.type.displayName
            )

            meal.items.forEach { item ->
                Text(
                    text = "${item.food.name} - ${item.quantityGrams} g"
                )
            }
        }
        val mealsOfSelectedDate = filterMealsByDate(
            meals = meals,
            date = selectedDate
        )

        val dailyNutrients = calculateDailyNutrients(
            mealsOfSelectedDate
        )

//        Text(
//            text = "Total do dia: $selectedDate"
//        )

        DateSelector(
            selectedDate = selectedDate,
            onDateChange = { newDate ->
                selectedDate = newDate
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



