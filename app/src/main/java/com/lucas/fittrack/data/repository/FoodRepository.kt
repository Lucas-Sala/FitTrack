package com.lucas.fittrack.data.repository

import com.lucas.fittrack.data.local.dao.FoodDao
import com.lucas.fittrack.data.mapper.toFood
import com.lucas.fittrack.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepository(
    private val foodDao: FoodDao
) {

    fun getAllFoods(): Flow<List<Food>> {
        return foodDao
            .getAllFoods()
            .map { entities ->
                entities.map { it.toFood() }
            }
    }

    suspend fun getFoodById(id: Long): Food? {
        return foodDao
            .getFoodById(id)
            ?.toFood()
    }

    fun getCategories(): Flow<List<String>> {
        return foodDao.getCategories()
    }

    fun searchFoods(
        query: String,
        category: String?
    ): Flow<List<Food>> {
        return foodDao
            .searchFoods(
                query = query,
                category = category
            )
            .map { entities ->
                entities.map { it.toFood() }
            }
    }
}