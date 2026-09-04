package com.lucas.fittrack.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.lucas.fittrack.data.local.dao.FoodDao
import com.lucas.fittrack.data.local.dao.MealDao
import com.lucas.fittrack.data.local.entity.FoodEntity
import com.lucas.fittrack.data.local.entity.MealEntity
import com.lucas.fittrack.data.local.entity.MealItemEntity

@Database(
    entities = [
        FoodEntity::class,
        MealEntity::class,
        MealItemEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao
    abstract fun mealDao(): MealDao
}