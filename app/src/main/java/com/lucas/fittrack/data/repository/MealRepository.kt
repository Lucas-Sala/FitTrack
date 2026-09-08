package com.lucas.fittrack.data.repository

import com.lucas.fittrack.data.local.dao.MealDao
import com.lucas.fittrack.data.mapper.toEntity
import com.lucas.fittrack.model.Meal
import com.lucas.fittrack.data.mapper.toMeal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealRepository(
    private val mealDao: MealDao
) {

    suspend fun insertMeal(meal: Meal) {

        val mealEntity = meal.toEntity()

        val itemEntities = meal.items.map { item ->
            item.toEntity()
        }

        mealDao.insertMealWithItems(
            meal = mealEntity,
            items = itemEntities
        )
    }

    fun getAllMeals(): Flow<List<Meal>> {
        return mealDao
            .getMealsWithItems()
            .map { mealsWithItems ->
                mealsWithItems.map { mealWithItems ->
                    mealWithItems.toMeal()
                }
            }
    }
}