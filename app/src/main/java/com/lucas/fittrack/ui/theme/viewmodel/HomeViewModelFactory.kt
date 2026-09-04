package com.lucas.fittrack.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucas.fittrack.data.repository.FoodRepository
import com.lucas.fittrack.data.repository.MealRepository

class HomeViewModelFactory(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                foodRepository = foodRepository,
                mealRepository = mealRepository
            ) as T
        }


        throw IllegalArgumentException("Unknown ViewModel class")
    }
}