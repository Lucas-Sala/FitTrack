package com.lucas.fittrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucas.fittrack.data.preferences.UserPreferencesRepository
import com.lucas.fittrack.data.repository.FoodRepository
import com.lucas.fittrack.data.repository.MealRepository

class HomeViewModelFactory(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                foodRepository = foodRepository,
                mealRepository = mealRepository,
                userPreferencesRepository = userPreferencesRepository
            ) as T
        }


        throw IllegalArgumentException("Unknown ViewModel class")
    }
}