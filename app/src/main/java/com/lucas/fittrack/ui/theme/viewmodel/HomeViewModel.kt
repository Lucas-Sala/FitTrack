package com.lucas.fittrack.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucas.fittrack.data.repository.FoodRepository
import com.lucas.fittrack.data.repository.MealRepository
import com.lucas.fittrack.model.Food
import com.lucas.fittrack.model.Meal
import kotlinx.coroutines.launch

class HomeViewModel(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository
) : ViewModel() {

    var foods by mutableStateOf<List<Food>>(emptyList())
        private set

    var meals by mutableStateOf<List<Meal>>(emptyList())
        private set

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            foodRepository.initializeDefaultFoods()

            foods = foodRepository.getAllFoods()
            meals = mealRepository.getAllMeals()
        }
    }

    fun saveMeal(meal: Meal) {
        viewModelScope.launch {
            mealRepository.insertMeal(meal)

            meals = mealRepository.getAllMeals()
        }
    }
}