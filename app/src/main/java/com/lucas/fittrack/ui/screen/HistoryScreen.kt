package com.lucas.fittrack.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import com.lucas.fittrack.ui.components.NutritionHistoryChart
import com.lucas.fittrack.ui.components.DateSelector
import com.lucas.fittrack.ui.components.HistoryPeriodSelector
import com.lucas.fittrack.ui.components.NutritionChartMetricSelector
import com.lucas.fittrack.ui.components.PeriodSummaryCard
import com.lucas.fittrack.ui.components.SectionCard
import com.lucas.fittrack.ui.theme.Dimens
import com.lucas.fittrack.ui.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            text = "Histórico",
            style = MaterialTheme.typography.headlineLarge
        )

        SectionCard {
            Text(
                text = "Período",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
            )

            HistoryPeriodSelector(
                selectedPeriod = uiState.historyPeriod,
                onPeriodSelected = { period ->
                    viewModel.selectHistoryPeriod(period)
                }
            )
        }

        DateSelector(
            selectedDate = uiState.selectedDate,
            onDateChange = viewModel::selectDate
        )

        SectionCard {
            Text(
                text = "Histórico Nutricional",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
            )

            NutritionHistoryChart(
                history = uiState.nutritionHistory,
                nutritionGoals = uiState.nutritionGoals,
                selectedMetric = uiState.nutritionChartMetric
            )

            NutritionChartMetricSelector(
                selectedMetric = uiState.nutritionChartMetric,
                onMetricSelected = viewModel::selectNutritionChartMetric
            )
        }

        PeriodSummaryCard(
            history = uiState.nutritionHistory
        )
    }
}