package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.lucas.fittrack.model.DailyNutrition
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
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import java.time.format.DateTimeFormatter

@Composable
fun CaloriesHistoryChart(
    history: List<DailyNutrition>,
    calorieGoal: Double,
    modifier: Modifier = Modifier
) {
//    if (history.isEmpty()) {
//        return
//    }

    val modelProducer = remember {
        CartesianChartModelProducer()
    }

    /*
     * Atualiza os dados sem recriar o producer.
     */
    LaunchedEffect(history) {
        if (history.isEmpty()) return@LaunchedEffect

        val xValues = history.indices.map { index ->
            index
        }

        val calories = history.map { day ->
            day.nutrients.calories
        }

        modelProducer.runTransaction {
            lineModel {
                series(
                    x = xValues,
                    y = calories
                )
            }
        }
    }

    if (history.isEmpty()) {
        return
    }

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
     * O formatter nunca deve retornar "".
     * Quem decide quais posições recebem labels
     * é o ItemPlacer.
     */
    val bottomAxisValueFormatter = remember(history) {
        CartesianValueFormatter { _, value, _ ->

            val index = value
                .toInt()
                .coerceIn(history.indices)

            history[index]
                .date
                .format(dateFormatter)
        }
    }

    val startAxisValueFormatter = remember {
        CartesianValueFormatter { _, value, _ ->
            "${value.toInt()} kcal"
        }
    }

    val markerGuideline = rememberAxisGuidelineComponent(
        Fill(MaterialTheme.colorScheme.onSurface),
        thickness = 2.dp
    )

    /*
     * Garante que a meta esteja dentro da faixa
     * vertical do gráfico.
     */
    val maxCalories = history.maxOfOrNull {
        it.nutrients.calories
    } ?: 0.0

    val chartMax = maxOf(
        maxCalories,
        calorieGoal
    ) * 1.10

    val rangeProvider = remember(chartMax) {
        CartesianLayerRangeProvider.fixed(
            minY = 0.0,
            maxY = chartMax
        )
    }

    val lineLayer = rememberLineCartesianLayer(
        rangeProvider = rangeProvider
    )

    val bottomAxis = HorizontalAxis.rememberBottom(
        valueFormatter = bottomAxisValueFormatter,
        itemPlacer = HorizontalAxis.ItemPlacer.aligned(
            spacing = {
                labelSpacing
            }
        )
    )

    val startAxis = VerticalAxis.rememberStart(
        valueFormatter = startAxisValueFormatter
    )

    /*
     * Formatter do marker.
     */
    val markerValueFormatter = remember(
        history,
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

                val index = point.entry.x
                    .toInt()
                    .coerceIn(history.indices)

                val day = history[index]

                val formattedDate =
                    day.date.format(dateFormatter)

                val calories =
                    day.nutrients.calories.toInt()

                "$formattedDate — $calories kcal"
            }
        }
    }

    /*
     * Usamos rememberAxisLabelComponent para
     * controlar a cor do texto do marker.
     */
    val marker = rememberDefaultCartesianMarker(
        label = rememberAxisLabelComponent(
            TextStyle(MaterialTheme.colorScheme.onSurface)
        ),
        valueFormatter = markerValueFormatter,
        guideline = markerGuideline
    )



    /*
     * Linha horizontal da meta.
     */
    val goalLine = HorizontalLine(
        y = {
            calorieGoal
        },
        line = rememberAxisGuidelineComponent(),
        labelComponent = rememberAxisLabelComponent(
            TextStyle(MaterialTheme.colorScheme.onSurface)
        ),
        label = {
            "Meta: ${calorieGoal.toInt()} kcal"
        }
    )

    CartesianChartHost(
        chart = rememberCartesianChart(
            lineLayer,
            startAxis = startAxis,
            bottomAxis = bottomAxis,
            marker = marker,
            decorations = listOf(goalLine)
        ),
        modelProducer = modelProducer,
        animationSpec = null,
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        scrollState = rememberVicoScrollState(
            scrollEnabled = false
        )
    )
}