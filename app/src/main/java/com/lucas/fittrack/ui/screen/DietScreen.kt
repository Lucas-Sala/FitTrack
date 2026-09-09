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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.lucas.fittrack.ui.components.SectionCard
import com.lucas.fittrack.ui.theme.Dimens
import com.lucas.fittrack.ui.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(
            Dimens.spacingLarge
        )
    ) {

        Text(
            text = "Dieta",
            style = MaterialTheme.typography.headlineLarge
        )

        DateSelector(
            selectedDate = uiState.selectedDate,
            onDateChange = viewModel::selectDate
        )

        SectionCard {

            Text(
                text = "Selecionar alimento",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
            )

            FoodList(
                foods = uiState.foods,
                onFoodSelected = { food ->
                    viewModel.selectFood(food)
                }
            )
        }

        uiState.selectedFood?.let { food ->

            SectionCard {

                Text(
                    text = "Alimento selecionado",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(
                        Dimens.spacingMedium
                    )
                )

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
        }

        SectionCard {

            Text(
                text = "Refeição atual",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
            )

            MealSummary(
                mealItems = uiState.mealItems
            )
        }

        SectionCard {

            Text(
                text = "Tipo da refeição",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
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

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingLarge
                )
            )

            Button(
                onClick = {
                    viewModel.saveCurrentMeal()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar refeição")
            }
        }
    }
}