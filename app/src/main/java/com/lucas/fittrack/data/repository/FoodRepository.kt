package com.lucas.fittrack.data.repository

import com.lucas.fittrack.data.local.dao.FoodDao
import com.lucas.fittrack.data.local.entity.FoodEntity
import com.lucas.fittrack.data.mapper.toFood
import com.lucas.fittrack.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepository(
    private val foodDao: FoodDao
) {

    suspend fun insert(food: FoodEntity) {
        foodDao.insert(food)
    }

    fun getAllFoods(): Flow<List<Food>> {
        return foodDao
            .getAll()
            .map { entities ->
                entities.map { entity ->
                    entity.toFood()
                }
            }
    }

    suspend fun initializeDefaultFoods() {

        if (foodDao.count() == 0) {

            foodDao.insert(
                FoodEntity(
                    name = "Arroz branco cozido",
                    caloriesPer100g = 128.0,
                    proteinPer100g = 2.5,
                    carbsPer100g = 28.1,
                    fatPer100g = 0.2
                )
            )

            foodDao.insert(
                FoodEntity(
                    name = "Feijão carioca cozido",
                    caloriesPer100g = 76.0,
                    proteinPer100g = 4.8,
                    carbsPer100g = 13.6,
                    fatPer100g = 0.5
                )
            )

            foodDao.insert(
                FoodEntity(
                    name = "Peito de frango grelhado",
                    caloriesPer100g = 159.0,
                    proteinPer100g = 32.0,
                    carbsPer100g = 0.0,
                    fatPer100g = 2.5
                )
            )

            foodDao.insert(
                FoodEntity(
                    name = "Banana",
                    caloriesPer100g = 92.0,
                    proteinPer100g = 1.4,
                    carbsPer100g = 23.8,
                    fatPer100g = 0.1
                )
            )
        }
    }
}