package com.lucas.fittrack.data.mapper

import com.lucas.fittrack.data.local.entity.FoodEntity
import com.lucas.fittrack.model.Food

fun FoodEntity.toFood(): Food {
    return Food(
        id = id,
        name = name,
        category = category,

        caloriesPer100g = caloriesPer100g,
        proteinPer100g = proteinPer100g,
        carbsPer100g = carbsPer100g,
        fatPer100g = fatPer100g,
        fiberPer100g = fiberPer100g,
        cholesterolPer100g = cholesterolPer100g
    )
}