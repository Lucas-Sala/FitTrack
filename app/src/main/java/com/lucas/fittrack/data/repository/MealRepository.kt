package com.lucas.fittrack.data.repository

import com.lucas.fittrack.data.local.dao.FoodDao
import com.lucas.fittrack.data.local.dao.MealDao
import com.lucas.fittrack.data.mapper.toEntity
import com.lucas.fittrack.model.Meal
import com.lucas.fittrack.data.mapper.toFood
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.data.mapper.toMeal

class MealRepository(
    private val mealDao: MealDao,
    private val foodDao: FoodDao
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

    suspend fun getAllMeals(): List<Meal> {

        val mealEntities = mealDao.getAllMeals()

        return mealEntities.map { mealEntity ->

            val itemEntities = mealDao.getItemsForMeal(
                mealEntity.id
            )

            val items = itemEntities.mapNotNull { itemEntity ->

                val foodEntity = foodDao.getById(
                    itemEntity.foodId
                )

                foodEntity?.let { entity ->
                    MealItem(
                        food = entity.toFood(),
                        quantityGrams = itemEntity.quantityGrams
                    )
                }
            }

            mealEntity.toMeal(items)
        }
    }
}