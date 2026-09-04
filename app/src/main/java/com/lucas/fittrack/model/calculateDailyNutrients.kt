package com.lucas.fittrack.model

fun calculateDailyNutrients(
    meals: List<Meal>
): Nutrients {

    val totalCalories = meals.sumOf { meal ->
        calculateMealNutrients(meal).calories
    }

    val totalProtein = meals.sumOf { meal ->
        calculateMealNutrients(meal).protein
    }

    val totalCarbs = meals.sumOf { meal ->
        calculateMealNutrients(meal).carbs
    }

    val totalFat = meals.sumOf { meal ->
        calculateMealNutrients(meal).fat
    }

    return Nutrients(
        calories = totalCalories,
        protein = totalProtein,
        carbs = totalCarbs,
        fat = totalFat
    )
}