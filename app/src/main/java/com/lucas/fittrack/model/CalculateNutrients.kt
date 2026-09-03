package com.lucas.fittrack.model

fun calculateNutrients(
    food: Food,
    quantityGrams: Double
): Nutrients {

    val factor = quantityGrams / 100.0

    return Nutrients(
        calories = food.caloriesPer100g * factor,
        protein = food.proteinPer100g * factor,
        carbs = food.carbsPer100g * factor,
        fat = food.fatPer100g * factor
    )
}