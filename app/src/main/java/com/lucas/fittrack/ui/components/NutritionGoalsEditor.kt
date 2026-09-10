package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import com.lucas.fittrack.ui.theme.Dimens

@Composable
fun NutritionGoalsEditor(
    caloriesText: String,
    proteinText: String,
    carbsText: String,
    fatText: String,
    fiberText: String,

    onCaloriesChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onFiberChange: (String) -> Unit,

    onSave: () -> Unit,

    errorMessage: String?


) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            Dimens.spacingMedium
        )
    ) {

        OutlinedTextField(
            value = caloriesText,
            onValueChange = onCaloriesChange,
            label = {
                Text("Calorias")
            },
            suffix = {
                Text("kcal")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = proteinText,
            onValueChange = onProteinChange,
            label = {
                Text("Proteínas")
            },
            suffix = {
                Text("g")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = carbsText,
            onValueChange = onCarbsChange,
            label = {
                Text("Carboidratos")
            },
            suffix = {
                Text("g")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fatText,
            onValueChange = onFatChange,
            label = {
                Text("Gorduras")
            },
            suffix = {
                Text("g")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fiberText,
            onValueChange = onFiberChange,
            label = {
                Text("Fibras")
            },
            suffix = {
                Text("g")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar metas")
        }
    }
}