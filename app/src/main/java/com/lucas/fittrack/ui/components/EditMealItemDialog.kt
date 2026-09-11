package com.lucas.fittrack.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.lucas.fittrack.model.MealItem

@Composable
fun EditMealItemDialog(
    item: MealItem,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    val focusManager =
        LocalFocusManager.current

    val keyboardController =
        LocalSoftwareKeyboardController.current

    var quantityFieldValue by remember(item) {
        mutableStateOf(
            TextFieldValue(
                text = item.quantityGrams.toString()
            )
        )
    }

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    val quantity =
        quantityFieldValue.text.toDoubleOrNull()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        quantityFieldValue = quantityFieldValue.copy(
            selection = TextRange(0, quantityFieldValue.text.length)
        )
    }

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
                    value = quantityFieldValue,

                    onValueChange = { newValue ->
                        quantityFieldValue =
                            newValue
                    },

                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            if (focusState.isFocused) {
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

                            if (
                                quantity != null &&
                                quantity > 0.0
                            ) {
                                focusManager.clearFocus()
                                keyboardController?.hide()

                                onConfirm(quantity)
                            }
                        }
                    ),

                    singleLine = true
                )
            }
        },

        confirmButton = {
            TextButton(
                enabled =
                    quantity != null &&
                            quantity > 0.0,

                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()

                    onConfirm(quantity!!)
                }
            ) {
                Text("Salvar")
            }
        },

        dismissButton = {
            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()

                    onDismiss()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}