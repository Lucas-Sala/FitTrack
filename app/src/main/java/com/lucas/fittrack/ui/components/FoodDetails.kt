package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
    val focusManager =
        LocalFocusManager.current

    val keyboardController =
        LocalSoftwareKeyboardController.current

    var quantityFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = quantityText
            )
        )
    }

    val quantity =
        quantityText.toDoubleOrNull() ?: 0.0

    val nutrients =
        calculateNutrients(
            food = food,
            quantityGrams = quantity
        )

    LaunchedEffect(quantityText) {
        if (
            quantityFieldValue.text != quantityText
        ) {
            quantityFieldValue =
                quantityFieldValue.copy(
                    text = quantityText,
                    selection = TextRange(
                        quantityText.length
                    )
                )
        }
    }

    LaunchedEffect(food.id) {
        focusManager.clearFocus()
    }

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

        OutlinedTextField(
            value = quantityFieldValue,

            onValueChange = { newValue ->

                quantityFieldValue =
                    newValue

                onQuantityChange(
                    newValue.text
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->

                    if (
                        focusState.isFocused
                    ) {
                        quantityFieldValue =
                            quantityFieldValue.copy(
                                selection = TextRange(
                                    start = 0,
                                    end = quantityFieldValue.text.length
                                )
                            )
                    }
                },

            label = {
                Text("Quantidade em gramas")
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),

            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),

            singleLine = true
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()

                onAdd()
            },
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