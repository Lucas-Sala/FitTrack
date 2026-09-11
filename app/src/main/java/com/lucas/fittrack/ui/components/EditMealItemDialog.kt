package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.lucas.fittrack.model.MealItem


@Composable
fun EditMealItemDialog(
    item: MealItem,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var quantityText by remember(item) {
        mutableStateOf(
            item.quantityGrams.toString()
        )
    }

    val quantity =
        quantityText.toDoubleOrNull()


    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Editar quantidade")
        },

        text = {
            Column {
                Text(
                    text = item.food.name
                )

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = {
                        quantityText = it
                    },
                    modifier = Modifier,
                    label = {
                        Text("Quantidade em gramas")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )
            }
        },

        confirmButton = {
            TextButton(
                enabled = quantity != null &&
                        quantity > 0.0,
                onClick = {
                    onConfirm(quantity!!)
                }
            ) {
                Text("Salvar")
            }
        },

        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}