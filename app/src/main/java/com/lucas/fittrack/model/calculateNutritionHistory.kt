package com.lucas.fittrack.model

import java.time.LocalDate

fun calculateNutritionHistory(
    meals: List<Meal>,
    startDate: LocalDate,
    endDate: LocalDate
): List<DailyNutrition> {

    if (endDate.isBefore(startDate)) {
        return emptyList()
    }

    val mealsByDate = meals.groupBy { meal ->
        meal.dateTime.toLocalDate()
    }

    val history = mutableListOf<DailyNutrition>()

    var currentDate = startDate

    while (!currentDate.isAfter(endDate)) {

        val mealsOfDay = mealsByDate[currentDate].orEmpty()

        val nutrients = calculateDailyNutrients(
            mealsOfDay
        )

        history.add(
            DailyNutrition(
                date = currentDate,
                nutrients = nutrients
            )
        )

        currentDate = currentDate.plusDays(1)
    }

    return history
}