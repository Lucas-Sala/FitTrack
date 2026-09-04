package com.lucas.fittrack.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "meal_items")
data class MealItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val mealId: Long,
    val foodId: Long,
    val quantityGrams: Double
)