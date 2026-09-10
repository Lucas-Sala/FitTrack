package com.lucas.fittrack.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.lucas.fittrack.model.DailyNutrition
import com.lucas.fittrack.model.NutritionChartMetric
import com.lucas.fittrack.model.NutritionGoals
import com.lucas.fittrack.ui.theme.Dimens
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import java.time.format.DateTimeFormatter
import java.util.Arrays.fill

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NutritionHistoryChart(
    history: List<DailyNutrition>,
    nutritionGoals: NutritionGoals,
    selectedMetric: NutritionChartMetric,
    modifier: Modifier = Modifier
) {

    val caloriesPercent = history.map { day ->
        if (nutritionGoals.calories > 0.0) {
            day.nutrients.calories / nutritionGoals.calories * 100.0
        } else {
            0.0
        }
    }

    val proteinPercent = history.map { day ->
        if (nutritionGoals.protein > 0.0) {
            day.nutrients.protein / nutritionGoals.protein * 100.0
        } else {
            0.0
        }
    }

    val carbsPercent = history.map { day ->
        if (nutritionGoals.carbs > 0.0) {
            day.nutrients.carbs / nutritionGoals.carbs * 100.0
        } else {
            0.0
        }
    }

    val fatPercent = history.map { day ->
        if (nutritionGoals.fat > 0.0) {
            day.nutrients.fat / nutritionGoals.fat * 100.0
        } else {
            0.0
        }
    }

    val fiberPercent = history.map { day ->
        if (nutritionGoals.fiber > 0.0) {
            day.nutrients.fiber / nutritionGoals.fiber * 100.0
        } else {
            0.0
        }
    }

    /*
     * Valores exibidos no gráfico.
     */
    val values = when (selectedMetric) {

        NutritionChartMetric.ALL ->
            caloriesPercent

        NutritionChartMetric.CALORIES ->
            history.map { it.nutrients.calories }

        NutritionChartMetric.PROTEIN ->
            history.map { it.nutrients.protein }

        NutritionChartMetric.CARBS ->
            history.map { it.nutrients.carbs }

        NutritionChartMetric.FAT ->
            history.map { it.nutrients.fat }

        NutritionChartMetric.FIBER ->
            history.map { it.nutrients.fiber }
    }

    /*
     * Meta correspondente à métrica selecionada.
     */
    val goalValue = when (selectedMetric) {

        NutritionChartMetric.ALL ->
            100.0

        NutritionChartMetric.CALORIES ->
            nutritionGoals.calories

        NutritionChartMetric.PROTEIN ->
            nutritionGoals.protein

        NutritionChartMetric.CARBS ->
            nutritionGoals.carbs

        NutritionChartMetric.FAT ->
            nutritionGoals.fat

        NutritionChartMetric.FIBER ->
            nutritionGoals.fiber
    }

    /*
     * Unidade utilizada no eixo Y, marker
     * e linha da meta.
     */
    val unit = when (selectedMetric) {

        NutritionChartMetric.ALL ->
            "%"

        NutritionChartMetric.CALORIES ->
            "kcal"

        NutritionChartMetric.PROTEIN,
        NutritionChartMetric.CARBS,
        NutritionChartMetric.FAT,
        NutritionChartMetric.FIBER ->
            "g"
    }

    var selectedIndex by remember(
        selectedMetric,
        history
    ) {
        mutableStateOf<Int?>(null)
    }

    val markerVisibilityListener = remember(history) {

        object : CartesianMarkerVisibilityListener {

            override fun onShown(
                marker: CartesianMarker,
                targets: List<CartesianMarker.Target>
            ) {
                updateSelectedIndex(targets)
            }

            override fun onUpdated(
                marker: CartesianMarker,
                targets: List<CartesianMarker.Target>
            ) {
                updateSelectedIndex(targets)
            }

            private fun updateSelectedIndex(
                targets: List<CartesianMarker.Target>
            ) {
                val target =
                    targets.firstOrNull()
                            as? LineCartesianLayerMarkerTarget
                        ?: return

                selectedIndex =
                    target.x
                        .toInt()
                        .coerceIn(history.indices)
            }
        }
    }

    key(selectedMetric) {

        val modelProducer = remember {
            CartesianChartModelProducer()
        }
        /*
         * Atualiza os dados no producer sempre
         * que a métrica ou o histórico mudar.
         */

        LaunchedEffect(
            history,
            values,
            caloriesPercent,
            proteinPercent,
            carbsPercent,
            fatPercent,
            fiberPercent
        ) {
            if (history.isEmpty()) {
                return@LaunchedEffect
            }

            val xValues = history.indices.toList()

            modelProducer.runTransaction {

                lineModel {

                    if (selectedMetric == NutritionChartMetric.ALL) {

                        series(
                            x = xValues,
                            y = caloriesPercent
                        )

                        series(
                            x = xValues,
                            y = proteinPercent
                        )

                        series(
                            x = xValues,
                            y = carbsPercent
                        )

                        series(
                            x = xValues,
                            y = fatPercent
                        )

                        series(
                            x = xValues,
                            y = fiberPercent
                        )

                    } else {

                        series(
                            x = xValues,
                            y = values
                        )
                    }
                }
            }
        }

        /*
         * Não exibe gráfico sem dados.
         */
        if (history.isEmpty()) {
            return
        }

        /*
         * Formatação das datas.
         */
        val dateFormatter = remember {
            DateTimeFormatter.ofPattern("dd/MM")
        }

        /*
         * Para 7 dias:
         * 1 label por ponto.
         *
         * Para 30 dias:
         * aproximadamente 1 label a cada 5 pontos.
         */
        val labelSpacing =
            if (history.size <= 7) {
                1
            } else {
                5
            }

        /*
         * Formatter do eixo X.
         */
        val bottomAxisValueFormatter =
            remember(history) {

                CartesianValueFormatter { _, value, _ ->

                    val index = value
                        .toInt()
                        .coerceIn(history.indices)

                    history[index]
                        .date
                        .format(dateFormatter)
                }
            }

        /*
         * Formatter do eixo Y.
         */
        val startAxisValueFormatter =
            remember(unit) {

                CartesianValueFormatter { _, value, _ ->

                    "${value.toInt()} $unit"
                }
            }

        /*
         * Linha vertical exibida pelo marker.
         */
        val markerGuideline =
            rememberAxisGuidelineComponent(
                Fill(
                    MaterialTheme.colorScheme.onSurface
                )
            )

        /*
         * Calcula o limite máximo do gráfico
         * usando tanto os dados quanto a meta.
         *
         * Acrescentamos 10% de espaço no topo.
         */
        val chartMax = if (
            selectedMetric == NutritionChartMetric.ALL
        ) {

            val allValues =
                caloriesPercent +
                        proteinPercent +
                        carbsPercent +
                        fatPercent +
                        fiberPercent

            val maxValue =
                allValues.maxOrNull() ?: 0.0

            maxOf(
                maxValue,
                100.0
            ) * 1.10

        } else {

            val maxValue =
                values.maxOrNull() ?: 0.0

            maxOf(
                maxValue,
                goalValue
            ) * 1.10
        }

        val caloriesLine =
            LineCartesianLayer.rememberLine(
                fill =
                    LineCartesianLayer.LineFill.single(
                        Fill(NutritionChartColors.Calories)
                    )
            )

        val proteinLine =
            LineCartesianLayer.rememberLine(
                fill =
                    LineCartesianLayer.LineFill.single(
                        Fill(NutritionChartColors.Protein)
                    )
            )

        val carbsLine =
            LineCartesianLayer.rememberLine(
                fill =
                    LineCartesianLayer.LineFill.single(
                        Fill(NutritionChartColors.Carbs)
                    )
            )

        val fatLine =
            LineCartesianLayer.rememberLine(
                fill =
                    LineCartesianLayer.LineFill.single(
                        Fill(NutritionChartColors.Fat)
                    )
            )

        val fiberLine =
            LineCartesianLayer.rememberLine(
                fill =
                    LineCartesianLayer.LineFill.single(
                        Fill(NutritionChartColors.Fiber)
                    )
            )

        val lineProvider =
            when (selectedMetric) {

                NutritionChartMetric.ALL ->
                    LineCartesianLayer.LineProvider.series(
                        caloriesLine,
                        proteinLine,
                        carbsLine,
                        fatLine,
                        fiberLine
                    )

                NutritionChartMetric.CALORIES ->
                    LineCartesianLayer.LineProvider.series(
                        caloriesLine
                    )

                NutritionChartMetric.PROTEIN ->
                    LineCartesianLayer.LineProvider.series(
                        proteinLine
                    )

                NutritionChartMetric.CARBS ->
                    LineCartesianLayer.LineProvider.series(
                        carbsLine
                    )

                NutritionChartMetric.FAT ->
                    LineCartesianLayer.LineProvider.series(
                        fatLine
                    )

                NutritionChartMetric.FIBER ->
                    LineCartesianLayer.LineProvider.series(
                        fiberLine
                    )
            }

        val lineLayer = key(
            selectedMetric,
            chartMax
        ) {

            rememberLineCartesianLayer(
                lineProvider = lineProvider,
                rangeProvider =
                    CartesianLayerRangeProvider.fixed(
                        minY = 0.0,
                        maxY = chartMax
                    )
            )
        }

        /*
         * Eixo X.
         */
        val bottomAxis =
            HorizontalAxis.rememberBottom(

                valueFormatter =
                    bottomAxisValueFormatter,

                itemPlacer =
                    HorizontalAxis.ItemPlacer.aligned(
                        spacing = {
                            labelSpacing
                        }
                    )
            )

        /*
         * Eixo Y.
         */
        val startAxis =
            VerticalAxis.rememberStart(

                valueFormatter =
                    startAxisValueFormatter
            )

        /*
         * Formatter do marker.
         */
        val markerValueFormatter = remember(
            history,
            values,
            unit,
            selectedMetric,
            caloriesPercent,
            proteinPercent,
            carbsPercent,
            fatPercent,
            fiberPercent,
            dateFormatter
        ) {

            DefaultCartesianMarker.ValueFormatter { _, targets ->

                val target =
                    targets.firstOrNull()
                            as? LineCartesianLayerMarkerTarget

                val point =
                    target
                        ?.points
                        ?.firstOrNull()

                if (point == null) {

                    "Sem dados"

                } else {

                    val index =
                        point.entry.x
                            .toInt()
                            .coerceIn(history.indices)

                    val formattedDate =
                        history[index]
                            .date
                            .format(dateFormatter)

                    if (
                        selectedMetric ==
                        NutritionChartMetric.ALL
                    ) {

                        if (selectedMetric == NutritionChartMetric.ALL) {

                            formattedDate

                        } else {

                            val value = values[index]

                            "$formattedDate — ${value.toInt()} $unit"
                        }

                    } else {

                        val value =
                            values[index]

                        "$formattedDate — ${value.toInt()} $unit"
                    }
                }
            }
        }

        /*
         * Marker.
         */
        val marker =
            rememberDefaultCartesianMarker(

                label =
                    rememberAxisLabelComponent(
                        TextStyle(
                            MaterialTheme.colorScheme.onSurface
                        )
                    ),

                valueFormatter =
                    markerValueFormatter,

                guideline =
                    markerGuideline
            )

        /*
         * Linha horizontal da meta.
         */
        val goalLine =
            HorizontalLine(

                y = {
                    goalValue
                },

                line =
                    rememberAxisGuidelineComponent(
                        Fill(
                            MaterialTheme.colorScheme.onSurface
                        )
                    ),

                labelComponent =
                    rememberAxisLabelComponent(
                        TextStyle(
                            MaterialTheme.colorScheme.onSurface
                        )
                    ),

                label = {
                    "Meta: ${goalValue.toInt()} $unit"
                }
            )

        val chart =
            rememberCartesianChart(
                lineLayer,
                startAxis = startAxis,
                bottomAxis = bottomAxis,
                marker = marker,
                markerVisibilityListener =
                    markerVisibilityListener,
                decorations = listOf(goalLine)
            )

        /*
         * Exibição do gráfico.
         */

        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement =
                Arrangement.spacedBy(
                    Dimens.spacingSmall
                )
        ) {

            CartesianChartHost(
                chart = chart,
                modelProducer = modelProducer,
                animationSpec = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                scrollState =
                    rememberVicoScrollState(
                        scrollEnabled = false
                    )
            )

            if (
                selectedMetric ==
                NutritionChartMetric.ALL &&
                selectedIndex != null
            ) {

                NutritionChartSelectionInfo(
                    day = history[selectedIndex!!],
                    nutritionGoals = nutritionGoals
                )
            }
        }
    }
}
