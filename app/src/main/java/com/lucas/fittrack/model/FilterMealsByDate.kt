package com.lucas.fittrack.model

import java.time.LocalDate

fun filterMealsByDate(
    meals: List<Meal>,
    date: LocalDate
): List<Meal> {

    return meals.filter { meal ->
        meal.dateTime.toLocalDate() == date
    }
}