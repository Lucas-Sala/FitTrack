package com.lucas.fittrack.ui.screen

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
import com.lucas.fittrack.ui.components.DateSelector
import com.lucas.fittrack.ui.components.FoodDetails
import com.lucas.fittrack.ui.components.FoodList
import com.lucas.fittrack.ui.components.MealSummary
import com.lucas.fittrack.ui.components.MealTypeSelector
import com.lucas.fittrack.ui.viewmodel.HomeViewModel

@Composable
fun DietScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var mealTypeMenuExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Dieta")

        DateSelector(
            selectedDate = uiState.selectedDate,
            onDateChange = { newDate ->
                viewModel.selectDate(newDate)
            }
        )

        Text("Selecione um alimento")

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
                onQuantityChange = { value ->
                    viewModel.updateQuantityText(value)
                },
                onAdd = {
                    viewModel.addFoodToCurrentMeal()
                }
            )
        }

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
    }
}