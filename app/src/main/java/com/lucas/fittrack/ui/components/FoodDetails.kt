package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.lucas.fittrack.model.Food
import com.lucas.fittrack.model.calculateNutrients
import com.lucas.fittrack.ui.theme.Dimens
import java.util.Locale

@Composable
fun FoodDetails(
    food: Food,
    quantityText: String,
    onQuantityChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    val quantity =
        quantityText.toDoubleOrNull() ?: 0.0

    val nutrients =
        calculateNutrients(
            food = food,
            quantityGrams = quantity
        )

    Column(
        verticalArrangement = Arrangement.spacedBy(
            Dimens.spacingMedium
        )
    ) {

        Text(
            text = food.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = quantityText,
            onValueChange = onQuantityChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Quantidade em gramas")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        HorizontalDivider()

        Column(
            verticalArrangement = Arrangement.spacedBy(
                Dimens.spacingSmall
            )
        ) {

            NutritionInfoRow(
                label = "Calorias",
                value = formatCalories(
                    nutrients.calories
                )
            )

            NutritionInfoRow(
                label = "Proteínas",
                value = formatGrams(
                    nutrients.protein
                )
            )

            NutritionInfoRow(
                label = "Carboidratos",
                value = formatGrams(
                    nutrients.carbs
                )
            )

            NutritionInfoRow(
                label = "Gorduras",
                value = formatGrams(
                    nutrients.fat
                )
            )

            NutritionInfoRow(
                label = "Fibras",
                value = formatGrams(
                    nutrients.fiber
                )
            )

            NutritionInfoRow(
                label = "Colesterol",
                value = formatMilligrams(
                    nutrients.cholesterol
                )
            )
        }

        Button(
            onClick = onAdd,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar à refeição")
        }
    }
}

private fun formatCalories(
    value: Double
): String {
    return String.format(
        Locale("pt", "BR"),
        "%.0f kcal",
        value
    )
}

private fun formatGrams(
    value: Double
): String {
    return String.format(
        Locale("pt", "BR"),
        "%.2f g",
        value
    )
}

private fun formatMilligrams(
    value: Double
): String {
    return String.format(
        Locale("pt", "BR"),
        "%.2f mg",
        value
    )
}