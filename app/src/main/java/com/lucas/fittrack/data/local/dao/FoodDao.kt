package com.lucas.fittrack.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.lucas.fittrack.data.local.entity.FoodEntity

@Dao
interface FoodDao {

    @Insert
    suspend fun insert(food: FoodEntity)

    @Query("SELECT * FROM foods")
    suspend fun getAll(): List<FoodEntity>

    @Query("SELECT COUNT(*) FROM foods")
    suspend fun count(): Int

    @Query("SELECT * FROM foods WHERE id = :id")
    suspend fun getById(id: Long): FoodEntity?
}