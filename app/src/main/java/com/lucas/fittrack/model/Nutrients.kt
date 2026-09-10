package com.lucas.fittrack.model

data class Nutrients(
    val calories: Double = 0.0,

    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,

    val fiber: Double = 0.0,
    val cholesterol: Double = 0.0,

    val vitamins: Vitamins = Vitamins(),
    val minerals: Minerals = Minerals()
)
