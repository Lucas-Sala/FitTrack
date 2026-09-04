package com.lucas.fittrack.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
        foods.forEach { food ->
            Text(
                text = food.name,
                modifier = Modifier
                    .clickable {
                        onFoodSelected(food)
                    }
                    .padding(8.dp)
            )
        }
    }
}

