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
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.MealType
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


class HomeViewModel(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository
) : ViewModel() {

    val foods = foodRepository
        .getAllFoods()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val meals = mealRepository
        .getAllMeals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var selectedFood by mutableStateOf<Food?>(null)
        private set

    var quantityText by mutableStateOf("")
        private set

    var mealItems by mutableStateOf<List<MealItem>>(emptyList())
        private set

    var selectedMealType by mutableStateOf(MealType.LUNCH)
        private set

    var selectedDate by mutableStateOf(LocalDate.now())
        private set

    init {
        initializeDefaultFoods()
    }

    private fun initializeDefaultFoods() {
        viewModelScope.launch {
            foodRepository.initializeDefaultFoods()
        }
    }

    fun selectFood(food: Food) {
        selectedFood = food
    }

    fun updateQuantityText(value: String) {
        quantityText = value
    }

    fun addFoodToCurrentMeal() {
        val food = selectedFood ?: return

        val quantity = quantityText.toDoubleOrNull() ?: return

        if (quantity <= 0.0) {
            return
        }

        val item = MealItem(
            food = food,
            quantityGrams = quantity
        )

        mealItems = mealItems + item
    }

    fun selectMealType(mealType: MealType) {
        selectedMealType = mealType
    }

    fun selectDate(date: LocalDate) {
        selectedDate = date
    }

    fun saveCurrentMeal() {

        if (mealItems.isEmpty()) {
            return
        }

        val currentTime = LocalDateTime.now().toLocalTime()

        val meal = Meal(
            type = selectedMealType,
            dateTime = selectedDate.atTime(currentTime),
            items = mealItems
        )


        viewModelScope.launch {
            mealRepository.insertMeal(meal)
            mealItems = emptyList()
        }
    }
}



