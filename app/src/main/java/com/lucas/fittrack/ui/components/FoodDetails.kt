package com.lucas.fittrack.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.lucas.fittrack.model.Food
import com.lucas.fittrack.model.calculateNutrients

@SuppressLint("DefaultLocale")
@Composable
fun FoodDetails(
    food: Food,
    quantityText: String,
    onQuantityChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    val quantity = quantityText.toDoubleOrNull() ?: 0.0

    val nutrients = calculateNutrients(
        food = food,
        quantityGrams = quantity
    )

    Column {
        Text(
            text = food.name,
            style = MaterialTheme.typography.titleSmall
        )
        OutlinedTextField(
            value = quantityText,
            onValueChange = { newValue ->
                onQuantityChange(newValue)
            },
            label = {
                Text("Quantidade em gramas")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
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

        Text(
            text = "Fibras: ${String.format("%.2f", nutrients.fiber)} g"
        )

        Text(
            text = "Colesterol: ${String.format("%.2f", nutrients.cholesterol)} mg"
        )

        Button(
            onClick = onAdd,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar à refeição")
        }
    }
}