package com.lucas.fittrack.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucas.fittrack.model.Meal
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
    onEditMeal: (Meal) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var mealToDelete by remember {
        mutableStateOf<Meal?>(null)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
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



            if (uiState.mealsOfSelectedDate.isEmpty()) {

                Text(
                    text = "Nenhuma Refeição",
                    style = MaterialTheme.typography.bodyMedium
                )

            } else {

                uiState.mealsOfSelectedDate.forEach { meal ->
                    MealCard(
                        meal = meal,

                        onEdit = {
                            onEditMeal(meal)
                        },
                        onDelete = {
                            mealToDelete = meal
                        }

                    )

                    Spacer(
                        modifier = Modifier.height(
                            Dimens.spacingSmall
                        )
                    )
                }
            }
        }
    }

    mealToDelete?.let { meal ->

        AlertDialog(
            onDismissRequest = {
                mealToDelete = null
            },

            title = {
                Text(
                    text = "Excluir refeição?"
                )
            },

            text = {
                Text(
                    text = "Tem certeza que deseja excluir esta refeição?"
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteMeal(meal)
                        mealToDelete = null
                    }
                ) {
                    Text("Excluir")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        mealToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

}