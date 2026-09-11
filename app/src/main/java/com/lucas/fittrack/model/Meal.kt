package com.lucas.fittrack.model

import java.time.LocalDateTime

data class Meal(
    val id: Long = 0,
    val type: MealType,
    val dateTime: LocalDateTime,
    val items: List<MealItem>
)