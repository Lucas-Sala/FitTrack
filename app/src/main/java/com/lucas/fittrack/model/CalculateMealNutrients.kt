package com.lucas.fittrack.model

fun calculateMealNutrients(
    meal: Meal
): Nutrients {

    return calculateMealNutrients(meal.items)
}

fun calculateMealNutrients(
    mealItems: List<MealItem>
): Nutrients {

    val totalCalories = mealItems.sumOf { item ->
        calculateNutrients(
            food = item.food,
            quantityGrams = item.quantityGrams
        ).calories
    }

    val totalProtein = mealItems.sumOf { item ->
        calculateNutrients(
            food = item.food,
            quantityGrams = item.quantityGrams
        ).protein
    }

    val totalCarbs = mealItems.sumOf { item ->
        calculateNutrients(
            food = item.food,
            quantityGrams = item.quantityGrams
        ).carbs
    }

    val totalFat = mealItems.sumOf { item ->
        calculateNutrients(
            food = item.food,
            quantityGrams = item.quantityGrams
        ).fat
    }

    return Nutrients(
        calories = totalCalories,
        protein = totalProtein,
        carbs = totalCarbs,
        fat = totalFat
    )
}