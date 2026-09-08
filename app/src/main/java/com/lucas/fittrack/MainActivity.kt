package com.lucas.fittrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucas.fittrack.ui.theme.FitTrackTheme
import com.lucas.fittrack.ui.theme.screen.HomeScreen
import com.lucas.fittrack.data.local.DatabaseProvider
import com.lucas.fittrack.data.repository.FoodRepository
import com.lucas.fittrack.data.repository.MealRepository
import com.lucas.fittrack.ui.theme.viewmodel.HomeViewModel
import com.lucas.fittrack.ui.theme.viewmodel.HomeViewModelFactory


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
        val homeViewModelFactory = HomeViewModelFactory(
            foodRepository = foodRepository,
            mealRepository = mealRepository
        )

        setContent {
            FitTrackTheme {

                val homeViewModel: HomeViewModel = viewModel(
                    factory = homeViewModelFactory
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    HomeScreen(
                        viewModel = homeViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FitTrackPreview() {
//    FitTrackTheme {
//        HomeScreen()
//    }
//}