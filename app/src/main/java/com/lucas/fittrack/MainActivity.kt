package com.lucas.fittrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.lucas.fittrack.model.Food
import com.lucas.fittrack.model.Nutrients
import com.lucas.fittrack.model.calculateNutrients
import com.lucas.fittrack.ui.theme.FitTrackTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.lucas.fittrack.model.sampleFoods
import androidx.compose.runtime.mutableStateListOf
import com.lucas.fittrack.model.Meal
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.MealType
import com.lucas.fittrack.model.calculateMealNutrients
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val mealItems = remember {
        mutableStateListOf<MealItem>()
    }

    var selectedFood by remember {
        mutableStateOf<Food?>(null)
    }

    var quantityText by remember {
        mutableStateOf("")
    }



    Column(
        modifier = modifier.padding(16.dp)
    ) {

        Text(
            text = "FitTrack"
        )

        Text(
            text = "Selecione um alimento:"
        )

        LazyColumn {
            items(sampleFoods) { food ->

                Text(
                    text = food.name,
                    modifier = Modifier
                        .clickable {
                            selectedFood = food
                        }
                        .padding(8.dp)
                )

            }
        }

        selectedFood?.let { food ->

            Text(
                text = "Selecionado: ${food.name}"
            )

            OutlinedTextField(
                value = quantityText,
                onValueChange = { newValue ->
                    quantityText = newValue
                },
                label = {
                    Text("Quantidade em gramas")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            val quantity = quantityText.toDoubleOrNull() ?: 0.0

            val nutrients = calculateNutrients(
                food = food,
                quantityGrams = quantity
            )

            Text(
                text = "Calorias: ${String.format("%.0f", nutrients.calories)} kcal"
            )

            Text(
                text = "Proteínas: ${String.format("%.2f", nutrients.protein)} g"
            )

            Text(
                text = "Carboidratos: ${String.format("%.2f", nutrients.carbs)} g"
            )

            Text(
                text = "Gorduras: ${String.format("%.2f", nutrients.fat)} g"
            )

            Button(
                onClick = {
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
            ) {
                Text("Adicionar à refeição")
            }
        }

        Text(
            text = "Itens da refeição:"
        )

        mealItems.forEach { item ->

            Text(
                text = "${item.food.name} - ${String.format("%.2f", item.quantityGrams)} g"
            )
        }
        val mealNutrients = calculateMealNutrients(mealItems)
//        val meal = Meal(
//            type = MealType.LUNCH,
//            dateTime = LocalDateTime.now(),
//            items = mealItems.toList()
//        )
//
//        val nutrients = calculateMealNutrients(meal)
        Text(
            text = "Total da refeição"
        )

        Text(
            text = "Calorias: ${String.format("%.0f", mealNutrients.calories)} kcal"
        )

        Text(
            text = "Proteínas: ${String.format("%.2f", mealNutrients.protein)} g"
        )

        Text(
            text = "Carboidratos: ${String.format("%.2f", mealNutrients.carbs)} g"
        )

        Text(
            text = "Gorduras: ${String.format("%.2f", mealNutrients.fat)} g"
        )
    }
}

//@Composable
//fun HomeScreen(modifier: Modifier = Modifier) {
//    val rice = Food(
//        name = "Arroz branco cozido",
//        caloriesPer100g = 128.0,
//        proteinPer100g = 2.5,
//        carbsPer100g = 28.1,
//        fatPer100g = 0.2
//    )
//    val food = sampleFoods[1]
//
//    var quantityText by remember {
//        mutableStateOf("180")
//    }
//
//    val quantity = quantityText.toDoubleOrNull() ?: 0.0
//
//    val nutrients = calculateNutrients(
//        food = food,
//        quantityGrams = quantity
//    )
//
//    Column(
//        modifier = modifier.padding(16.dp)
//    ) {
//        Text(
//            text = "FitTrack"
//        )
//
//        Text(
//            text = "Nome: ${food.name}"
//        )
//
//        OutlinedTextField(
//            value = quantityText,
//            onValueChange = { newValue ->
//                quantityText = newValue
//            },
//            label = {
//                Text("Quantidade em gramas")
//            },
//
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Decimal
//            )
//        )
//
//        Text(
//            text = "Calorias: ${String.format("%.0f", nutrients.calories)} kcal"
//        )
//
//        Text(
//            text = "Proteínas: ${String.format("%.2f", nutrients.protein)} g"
//        )
//
//        Text(
//            text = "Carboidratos: ${String.format("%.2f", nutrients.carbs)} g"
//        )
//
//        Text(
//            text = "Gorduras: ${String.format("%.2f", nutrients.fat)} g"
//        )
//
//        Button(
//            onClick = {
//                // ação futura
//            }
//        ) {
//            Text("Adicionar refeição")
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun FitTrackPreview() {
    FitTrackTheme {
        HomeScreen()
    }
}