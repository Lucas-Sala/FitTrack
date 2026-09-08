package com.lucas.fittrack.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.lucas.fittrack.data.local.entity.FoodEntity
import com.lucas.fittrack.data.local.entity.MealItemEntity

data class MealItemWithFood(

    @Embedded
    val mealItem: MealItemEntity,

    @Relation(
        parentColumns = ["foodId"],
        entityColumns = ["id"],
    )
    val food: FoodEntity
)