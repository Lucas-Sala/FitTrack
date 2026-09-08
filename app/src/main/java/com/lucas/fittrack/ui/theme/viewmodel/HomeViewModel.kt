package com.lucas.fittrack.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucas.fittrack.data.preferences.UserPreferencesRepository
import com.lucas.fittrack.data.repository.FoodRepository
import com.lucas.fittrack.data.repository.MealRepository
import com.lucas.fittrack.model.Food
import com.lucas.fittrack.model.Meal
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.MealType
import com.lucas.fittrack.model.NutritionGoals
import com.lucas.fittrack.model.calculateDailyNutrients
import com.lucas.fittrack.model.filterMealsByDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine



class HomeViewModel(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _editableState = MutableStateFlow(
        HomeUiState()
    )

    val uiState: StateFlow<HomeUiState> =
        combine(
            foodRepository.getAllFoods(),
            mealRepository.getAllMeals(),
            _editableState,
            userPreferencesRepository.nutritionGoals
        ) { foods, meals, editableState, nutritionGoals ->

            val mealsOfSelectedDate = filterMealsByDate(
                meals = meals,
                date = editableState.selectedDate
            )

            val dailyNutrients = calculateDailyNutrients(
                mealsOfSelectedDate
            )

            editableState.copy(
                foods = foods,
                meals = meals,
                mealsOfSelectedDate = mealsOfSelectedDate,
                dailyNutrients = dailyNutrients,
                nutritionGoals = nutritionGoals
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

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

    var mealItems by mutableStateOf<List<MealItem>>(emptyList())
        private set

    private var goalsInitialized = false

    init {
        viewModelScope.launch {
            foodRepository.initializeDefaultFoods()
        }

        viewModelScope.launch {
            userPreferencesRepository.nutritionGoals.collect { goals ->

                if (!goalsInitialized) {
                    _editableState.value =
                        _editableState.value.copy(
                            caloriesGoalText = goals.calories.toString(),
                            proteinGoalText = goals.protein.toString(),
                            carbsGoalText = goals.carbs.toString(),
                            fatGoalText = goals.fat.toString()
                        )

                    goalsInitialized = true
                }
            }
        }
    }

    private fun initializeDefaultFoods() {
        viewModelScope.launch {
            foodRepository.initializeDefaultFoods()
        }
    }

    fun selectFood(food: Food) {
        _editableState.value = _editableState.value.copy(
            selectedFood = food
        )
    }

    fun updateQuantityText(value: String) {
        _editableState.value = _editableState.value.copy(
            quantityText = value
        )
    }

    fun addFoodToCurrentMeal() {

        val currentState = _editableState.value

        val food = currentState.selectedFood ?: return

        val quantity =
            currentState.quantityText.toDoubleOrNull() ?: return

        if (quantity <= 0.0) {
            return
        }

        val item = MealItem(
            food = food,
            quantityGrams = quantity
        )

        _editableState.value = currentState.copy(
            mealItems = currentState.mealItems + item
        )
    }

    fun selectMealType(mealType: MealType) {
        _editableState.value = _editableState.value.copy(
            selectedMealType = mealType
        )
    }

    fun selectDate(date: LocalDate) {
        _editableState.value = _editableState.value.copy(
            selectedDate = date
        )
    }

    fun saveCurrentMeal() {

        val currentState = _editableState.value

        if (currentState.mealItems.isEmpty()) {
            return
        }

        val currentTime = LocalDateTime.now().toLocalTime()

        val meal = Meal(
            type = currentState.selectedMealType,
            dateTime = currentState.selectedDate.atTime(currentTime),
            items = currentState.mealItems
        )

        viewModelScope.launch {

            mealRepository.insertMeal(meal)

            _editableState.value = _editableState.value.copy(
                mealItems = emptyList()
            )
        }
    }

    fun saveNutritionGoals() {

        val state = _editableState.value

        val calories =
            state.caloriesGoalText.toDoubleOrNull()

        val protein =
            state.proteinGoalText.toDoubleOrNull()

        val carbs =
            state.carbsGoalText.toDoubleOrNull()

        val fat =
            state.fatGoalText.toDoubleOrNull()

        if (
            calories == null ||
            protein == null ||
            carbs == null ||
            fat == null
        ) {
            _editableState.value =
                state.copy(
                    nutritionGoalsError =
                        "Preencha todas as metas com valores numéricos válidos."
                )

            return
        }

        if (
            calories <= 0.0 ||
            protein <= 0.0 ||
            carbs <= 0.0 ||
            fat <= 0.0
        ) {
            _editableState.value =
                state.copy(
                    nutritionGoalsError =
                        "As metas devem ser maiores que zero."
                )

            return
        }

        val goals = NutritionGoals(
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat
        )

        viewModelScope.launch {
            userPreferencesRepository.saveNutritionGoals(goals)

            _editableState.value =
                _editableState.value.copy(
                    nutritionGoalsError = null
                )
        }
    }

    fun updateCaloriesGoalText(value: String) {
        _editableState.value =
            _editableState.value.copy(
                caloriesGoalText = value,
                nutritionGoalsError = null
            )
    }

    fun updateProteinGoalText(value: String) {
        _editableState.value =
            _editableState.value.copy(
                proteinGoalText = value,
                nutritionGoalsError = null
            )
    }

    fun updateCarbsGoalText(value: String) {
        _editableState.value =
            _editableState.value.copy(
                carbsGoalText = value,
                nutritionGoalsError = null
            )
    }

    fun updateFatGoalText(value: String) {
        _editableState.value =
            _editableState.value.copy(
                fatGoalText = value,
                nutritionGoalsError = null
            )
    }
}



