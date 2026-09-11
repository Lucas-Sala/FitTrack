package com.lucas.fittrack.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.calculateMealNutrients
import com.lucas.fittrack.ui.theme.Dimens
import java.util.Locale



@Composable
fun MealSummary(
    mealItems: List<MealItem>,
    onRemoveItem: (MealItem) -> Unit,
    onEditItem: (MealItem) -> Unit
) {
    val mealNutrients =
        calculateMealNutrients(mealItems)

    Column(
        verticalArrangement = Arrangement.spacedBy(
            Dimens.spacingMedium
        )
    ) {

        Text(
            text = "Itens da refeição",
            style = MaterialTheme.typography.labelLarge
        )

        if (mealItems.isEmpty()) {

            Text(
                text = "Nenhum alimento adicionado.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        } else {

            Column(
                verticalArrangement = Arrangement.spacedBy(
                    Dimens.spacingSmall
                )
            ) {

                mealItems.forEach { item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEditItem(item)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            Dimens.spacingSmall
                        )
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = item.food.name,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = formatQuantity(item.quantityGrams),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = {
                                onRemoveItem(item)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = "Remover alimento"
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider()

        Text(
            text = "Total da refeição",
            style = MaterialTheme.typography.labelLarge
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(
                Dimens.spacingSmall
            )
        ) {

            NutritionInfoRow(
                label = "Calorias",
                value = formatCalories(
                    mealNutrients.calories
                )
            )

            NutritionInfoRow(
                label = "Proteínas",
                value = formatGrams(
                    mealNutrients.protein
                )
            )

            NutritionInfoRow(
                label = "Carboidratos",
                value = formatGrams(
                    mealNutrients.carbs
                )
            )

            NutritionInfoRow(
                label = "Gorduras",
                value = formatGrams(
                    mealNutrients.fat
                )
            )

            NutritionInfoRow(
                label = "Fibras",
                value = formatGrams(
                    mealNutrients.fiber
                )
            )

            NutritionInfoRow(
                label = "Colesterol",
                value = formatMilligrams(
                    mealNutrients.cholesterol
                )
            )
        }
    }
}

private fun formatQuantity(
    value: Double
): String {
    return String.format(
        Locale("pt", "BR"),
        "%.0f g",
        value
    )
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