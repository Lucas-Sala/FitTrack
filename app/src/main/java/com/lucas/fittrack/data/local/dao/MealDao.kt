package com.lucas.fittrack.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import com.lucas.fittrack.data.local.entity.MealEntity
import com.lucas.fittrack.data.local.entity.MealItemEntity
import com.lucas.fittrack.data.local.relation.MealWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert
    suspend fun insertMeal(meal: MealEntity): Long

    @Insert
    suspend fun insertMealItems(items: List<MealItemEntity>)

    @Transaction
    suspend fun insertMealWithItems(
        meal: MealEntity,
        items: List<MealItemEntity>,
    ) {
        val mealId = insertMeal(meal)

        val itemsWithMealId = items.map { item ->
            item.copy(mealId = mealId)
        }

        insertMealItems(itemsWithMealId)
    }

    @Transaction
    @Query("SELECT * FROM meals")
    fun getMealsWithItems(): Flow<List<MealWithItems>>
}