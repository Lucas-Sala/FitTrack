package com.lucas.fittrack.model

import java.time.LocalDate

data class DailyNutrition(
    val date: LocalDate,
    val nutrients: Nutrients
)