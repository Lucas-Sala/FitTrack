package com.lucas.fittrack.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucas.fittrack.ui.components.NutritionGoalsEditor
import com.lucas.fittrack.ui.components.SectionCard
import com.lucas.fittrack.ui.theme.Dimens
import com.lucas.fittrack.ui.viewmodel.HomeViewModel

@Composable
fun SettingsScreen(
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
            text = "Configurações",
            style = MaterialTheme.typography.headlineLarge
        )

        SectionCard {

            Text(
                text = "Metas nutricionais",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Defina seus objetivos diários de calorias e macronutrientes.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingLarge
                )
            )

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
}