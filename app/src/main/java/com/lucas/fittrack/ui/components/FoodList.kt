package com.lucas.fittrack.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucas.fittrack.model.Food

@Composable
fun FoodList(
    foods: List<Food>,
    onFoodSelected: (Food) -> Unit
) {
    Column {
        HorizontalDivider()
        foods.forEach { food ->

            Text(
                text = food.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFoodSelected(food)
                    }
                    .padding(
                        vertical = 12.dp
                    )
            )
        }
    }
}

