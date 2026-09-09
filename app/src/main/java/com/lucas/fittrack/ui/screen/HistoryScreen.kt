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
import com.lucas.fittrack.ui.components.CaloriesHistoryChart
import com.lucas.fittrack.ui.components.DateSelector
import com.lucas.fittrack.ui.components.HistoryPeriodSelector
import com.lucas.fittrack.ui.viewmodel.HomeViewModel

@Composable
fun HistoryScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Histórico")

        DateSelector(
            selectedDate = uiState.selectedDate,
            onDateChange = { newDate ->
                viewModel.selectDate(newDate)
            }
        )

        HistoryPeriodSelector(
            selectedPeriod = uiState.historyPeriod,
            onPeriodSelected = { period ->
                viewModel.selectHistoryPeriod(period)
            }
        )

        CaloriesHistoryChart(
            history = uiState.nutritionHistory,
            calorieGoal = uiState.nutritionGoals.calories
        )
    }
}