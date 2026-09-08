package com.lucas.fittrack.data.mapper

import com.lucas.fittrack.data.local.entity.MealEntity
import com.lucas.fittrack.data.local.entity.MealItemEntity
import com.lucas.fittrack.data.local.relation.MealWithItems
import com.lucas.fittrack.model.Meal
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.MealType
import java.time.LocalDateTime

fun Meal.toEntity(): MealEntity {
    return MealEntity(
        type = type.name,
        dateTime = dateTime.toString()
    )
}

fun MealItem.toEntity(): MealItemEntity {
    return MealItemEntity(
        mealId = 0,
        foodId = food.id,
        quantityGrams = quantityGrams
    )
}

fun MealEntity.toMeal(
    items: List<MealItem>
): Meal {
    return Meal(
        type = MealType.valueOf(type),
        dateTime = LocalDateTime.parse(dateTime),
        items = items
    )
}

fun MealWithItems.toMeal(): Meal {

    val mealItems = items.map { itemWithFood ->

        MealItem(
            food = itemWithFood.food.toFood(),
            quantityGrams = itemWithFood.mealItem.quantityGrams
        )
    }

    return Meal(
        type = MealType.valueOf(meal.type),
        dateTime = LocalDateTime.parse(meal.dateTime),
        items = mealItems
    )
}