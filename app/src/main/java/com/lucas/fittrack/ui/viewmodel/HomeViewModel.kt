package com.lucas.fittrack.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucas.fittrack.data.local.importer.TacoImporter
import com.lucas.fittrack.data.preferences.UserPreferencesRepository
import com.lucas.fittrack.data.repository.FoodRepository
import com.lucas.fittrack.data.repository.MealRepository
import com.lucas.fittrack.model.Food
import com.lucas.fittrack.model.HistoryPeriod
import com.lucas.fittrack.model.Meal
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.model.MealType
import com.lucas.fittrack.model.NutritionChartMetric
import com.lucas.fittrack.model.NutritionGoals
import com.lucas.fittrack.model.calculateDailyNutrients
import com.lucas.fittrack.model.calculateNutritionHistory
import com.lucas.fittrack.model.filterMealsByDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map


@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val tacoImporter: TacoImporter
) : ViewModel() {
    private val _editableState = MutableStateFlow(
        HomeUiState()
    )

    private val filteredFoods =
        _editableState
            .map { state ->
                state.foodSearchText to state.selectedFoodCategory
            }
            .distinctUntilChanged()
            .flatMapLatest { (query, category) ->
                foodRepository.searchFoods(
                    query = query.trim(),
                    category = category
                )
            }

    private val foodCategories =
        foodRepository.getCategories()

    val uiState: StateFlow<HomeUiState> =
        combine(
            filteredFoods,
            foodCategories,
            mealRepository.getAllMeals(),
            _editableState,
            userPreferencesRepository.nutritionGoals
        ) { foods, categories, meals, editableState, nutritionGoals ->

            val mealsOfSelectedDate = filterMealsByDate(
                meals = meals,
                date = editableState.selectedDate
            )

            val historyEndDate = editableState.selectedDate

            val historyStartDate = historyEndDate.minusDays(
                editableState.historyPeriod.days - 1
            )

            val nutritionHistory = calculateNutritionHistory(
                meals = meals,
                startDate = historyStartDate,
                endDate = historyEndDate
            )

            val dailyNutrients = calculateDailyNutrients(
                mealsOfSelectedDate
            )

            editableState.copy(
                foods = foods,
                foodCategories = categories,
                meals = meals,
                mealsOfSelectedDate = mealsOfSelectedDate,
                dailyNutrients = dailyNutrients,
                nutritionGoals = nutritionGoals,
                nutritionHistory = nutritionHistory
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState()
            )

    private var goalsInitialized = false

    init {
        viewModelScope.launch {
            tacoImporter.importIfNeeded()
        }

        viewModelScope.launch {
            userPreferencesRepository.nutritionGoals.collect { goals ->

                if (!goalsInitialized) {
                    _editableState.value =
                        _editableState.value.copy(
                            caloriesGoalText = goals.calories.toString(),
                            proteinGoalText = goals.protein.toString(),
                            carbsGoalText = goals.carbs.toString(),
                            fatGoalText = goals.fat.toString(),
                            fiberGoalText = goals.fiber.toString()
                        )

                    goalsInitialized = true
                }
            }
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

        val fiber =
            state.fiberGoalText.toDoubleOrNull()

        if (
            calories == null ||
            protein == null ||
            carbs == null ||
            fat == null ||
            fiber == null
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
            fat <= 0.0 ||
            fiber <= 0.0
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
            fat = fat,
            fiber = fiber
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

    fun updateFiberGoalText(value: String) {
        _editableState.value =
            _editableState.value.copy(
                fiberGoalText = value,
                nutritionGoalsError = null
            )
    }

    fun selectHistoryPeriod(period: HistoryPeriod) {
        _editableState.value = _editableState.value.copy(
            historyPeriod = period
        )
    }

    fun updateFoodSearchText(value: String) {
        _editableState.value =
            _editableState.value.copy(
                foodSearchText = value
            )
    }

    fun selectFoodCategory(category: String?) {
        _editableState.value =
            _editableState.value.copy(
                selectedFoodCategory = category
            )
    }

    fun selectNutritionChartMetric(
        metric: NutritionChartMetric
    ) {
        _editableState.value =
            _editableState.value.copy(
                nutritionChartMetric = metric
            )
    }
}



