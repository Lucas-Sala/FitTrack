package com.lucas.fittrack.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucas.fittrack.ui.components.NutritionGoalsEditor
import com.lucas.fittrack.ui.viewmodel.HomeViewModel

@Composable
fun SettingsScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Configurações")

        NutritionGoalsEditor(
            caloriesText = uiState.caloriesGoalText,
            proteinText = uiState.proteinGoalText,
            carbsText = uiState.carbsGoalText,
            fatText = uiState.fatGoalText,

            onCaloriesChange = {
                viewModel.updateCaloriesGoalText(it)
            },

            onProteinChange = {
                viewModel.updateProteinGoalText(it)
            },

            onCarbsChange = {
                viewModel.updateCarbsGoalText(it)
            },

            onFatChange = {
                viewModel.updateFatGoalText(it)
            },

            onSave = {
                viewModel.saveNutritionGoals()
            },

            errorMessage = uiState.nutritionGoalsError
        )
    }
}