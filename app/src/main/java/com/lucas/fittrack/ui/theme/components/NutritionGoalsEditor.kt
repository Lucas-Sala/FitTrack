package com.lucas.fittrack.ui.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NutritionGoalsEditor(
    caloriesText: String,
    proteinText: String,
    carbsText: String,
    fatText: String,

    onCaloriesChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    onFatChange: (String) -> Unit,

    onSave: () -> Unit,
    errorMessage: String?
) {
    Column {

        Text(
            text = "Metas nutricionais"
        )

        OutlinedTextField(
            value = caloriesText,
            onValueChange = onCaloriesChange,
            label = {
                Text("Calorias")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        OutlinedTextField(
            value = proteinText,
            onValueChange = onProteinChange,
            label = {
                Text("Proteínas (g)")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        OutlinedTextField(
            value = carbsText,
            onValueChange = onCarbsChange,
            label = {
                Text("Carboidratos (g)")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        OutlinedTextField(
            value = fatText,
            onValueChange = onFatChange,
            label = {
                Text("Gorduras (g)")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        errorMessage?.let { message ->
            Text(
                text = message
            )
        }

        Button(
            onClick = onSave
        ) {
            Text("Salvar metas")
        }
    }
}