package com.lucas.fittrack.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import com.lucas.fittrack.model.DailyNutrition
import com.lucas.fittrack.model.Food
import com.lucas.fittrack.model.HistoryPeriod
import com.lucas.fittrack.model.Meal
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.MealType
import com.lucas.fittrack.model.Nutrients
import com.lucas.fittrack.model.NutritionChartMetric
import java.time.LocalDate
import com.lucas.fittrack.model.NutritionGoals

data class HomeUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val foods: List<Food> = emptyList(),
    val meals: List<Meal> = emptyList(),

    val selectedFood: Food? = null,
    val quantityText: String = "",
    val mealItems: List<MealItem> = emptyList(),
    val selectedMealType: MealType = MealType.LUNCH,
    val selectedDate: LocalDate = LocalDate.now(),

    val mealsOfSelectedDate: List<Meal> = emptyList(),

    val dailyNutrients: Nutrients = Nutrients(
        calories = 0.0,
        protein = 0.0,
        carbs = 0.0,
        fat = 0.0,
        fiber = 0.0
    ),

    val nutritionGoals: NutritionGoals = NutritionGoals(),

    val caloriesGoalText: String = "",
    val proteinGoalText: String = "",
    val carbsGoalText: String = "",
    val fatGoalText: String = "",
    val fiberGoalText: String = "",

    val nutritionGoalsError: String? = null,

    val nutritionHistory: List<DailyNutrition> = emptyList(),
    val historyPeriod: HistoryPeriod = HistoryPeriod.SEVEN_DAYS,

    val foodSearchText: String = "",
    val selectedFoodCategory: String? = null,
    val foodCategories: List<String> = emptyList(),
    val nutritionChartMetric: NutritionChartMetric =
        NutritionChartMetric.ALL
)