package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.calculateMealNutrients
import kotlin.collections.forEach

@Composable
fun MealSummary(
    mealItems: List<MealItem>
) {
    val mealNutrients = calculateMealNutrients(mealItems)

    Column {
        Text(
            text = "Itens da refeição:"
        )

        mealItems.forEach { item ->
            Text(
                text = "${item.food.name} - ${item.quantityGrams} g"
            )
        }

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

        Text(
            text = "Fibras: ${String.format("%.2f", mealNutrients.fiber)} g"
        )

        Text(
            text = "Colesterol: ${String.format("%.2f", mealNutrients.cholesterol)} mg"
        )
    }
}

