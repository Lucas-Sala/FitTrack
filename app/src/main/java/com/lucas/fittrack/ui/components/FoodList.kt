package com.lucas.fittrack.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    HorizontalDivider()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 150.dp)
    ) {
        items(
            items = foods,
            key = { food -> food.id }
        ) { food ->

            Text(
                text = food.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFoodSelected(food)
                    }
                    .padding(
                        vertical = 6.dp
                    )
            )
        }
    }
}