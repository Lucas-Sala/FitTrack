package com.lucas.fittrack.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.lucas.fittrack.model.MealType

@Composable
fun MealTypeSelector(
    selectedMealType: MealType,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onMealTypeSelected: (MealType) -> Unit
) {
    Button(
        onClick = {
            onExpandedChange(true)
        }
    ) {
        Text(selectedMealType.displayName)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            onExpandedChange(false)
        }
    ) {
        MealType.entries.forEach { mealType ->

            DropdownMenuItem(
                text = {
                    Text(mealType.displayName)
                },
                onClick = {
                    onMealTypeSelected(mealType)
                    onExpandedChange(false)
                }
            )
        }
    }
}