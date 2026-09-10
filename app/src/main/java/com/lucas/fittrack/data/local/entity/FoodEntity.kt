package com.lucas.fittrack.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey
    val id: Long,

    val name: String,
    val category: String,

    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,

    val fiberPer100g: Double,
    val cholesterolPer100g: Double
)