package com.lucas.fittrack.model

import java.time.LocalDateTime

data class Meal(
    val type: MealType,
    val dateTime: LocalDateTime,
    val items: List<MealItem>
)