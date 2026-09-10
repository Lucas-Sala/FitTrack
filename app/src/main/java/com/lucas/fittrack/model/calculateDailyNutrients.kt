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

    val totalFiber = meals.sumOf { meal ->
        calculateMealNutrients(meal).fiber
    }

    val totalCholesterol = meals.sumOf { meal ->
        calculateMealNutrients(meal).cholesterol
    }

    return Nutrients(
        calories = totalCalories,
        protein = totalProtein,
        carbs = totalCarbs,
        fat = totalFat,
        fiber = totalFiber,
        cholesterol = totalCholesterol
    )
}