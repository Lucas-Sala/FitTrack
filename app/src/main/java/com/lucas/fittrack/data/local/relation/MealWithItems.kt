package com.lucas.fittrack.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.lucas.fittrack.data.local.entity.MealEntity
import com.lucas.fittrack.data.local.entity.MealItemEntity

data class MealWithItems(

    @Embedded
    val meal: MealEntity,

    @Relation(
        entity = MealItemEntity::class,
        parentColumns = ["id"],
        entityColumns = ["mealId"],
    )
    val items: List<MealItemWithFood>
)