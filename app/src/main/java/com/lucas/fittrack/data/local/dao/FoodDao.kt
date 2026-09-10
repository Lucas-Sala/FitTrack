package com.lucas.fittrack.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.lucas.fittrack.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM foods ORDER BY name ASC")
    fun getAllFoods(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM foods WHERE id = :id")
    suspend fun getFoodById(id: Long): FoodEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<FoodEntity>)

    @Query("SELECT COUNT(*) FROM foods")
    suspend fun countFoods(): Int

    @Query("DELETE FROM foods")
    suspend fun deleteAllFoods()

    @Query("""
    SELECT * FROM foods
    WHERE (:category IS NULL OR category = :category)
      AND name LIKE '%' || :query || '%'
    ORDER BY name ASC
""")
    fun searchFoods(
        query: String,
        category: String?
    ): Flow<List<FoodEntity>>

    @Query(
        """
        SELECT DISTINCT category
        FROM foods
        ORDER BY category ASC
    """
    )
    fun getCategories(): Flow<List<String>>
}