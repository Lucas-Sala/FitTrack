package com.lucas.fittrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucas.fittrack.ui.theme.FitTrackTheme
import com.lucas.fittrack.data.local.DatabaseProvider
import com.lucas.fittrack.data.preferences.UserPreferencesRepository
import com.lucas.fittrack.data.repository.FoodRepository
import com.lucas.fittrack.data.repository.MealRepository
import com.lucas.fittrack.ui.FitTrackApp
import com.lucas.fittrack.ui.viewmodel.HomeViewModel
import com.lucas.fittrack.ui.viewmodel.HomeViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = DatabaseProvider.getDatabase(this)

        val foodRepository = FoodRepository(
            foodDao = database.foodDao()
        )
        val mealRepository = MealRepository(
            mealDao = database.mealDao()
        )

        val userPreferencesRepository =
            UserPreferencesRepository(
                context = applicationContext
            )

        val homeViewModelFactory = HomeViewModelFactory(
            foodRepository = foodRepository,
            mealRepository = mealRepository,
            userPreferencesRepository = userPreferencesRepository
        )



        setContent {
            FitTrackTheme {

                val homeViewModel: HomeViewModel =
                    viewModel(
                        factory = homeViewModelFactory
                    )

                FitTrackApp(
                    viewModel = homeViewModel
                )
            }
        }
    }
}

