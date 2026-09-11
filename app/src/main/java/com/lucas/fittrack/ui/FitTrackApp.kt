package com.lucas.fittrack.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lucas.fittrack.ui.navigation.AppDestination
import com.lucas.fittrack.ui.navigation.NavigationItem
import com.lucas.fittrack.ui.screen.DietScreen
import com.lucas.fittrack.ui.screen.HistoryScreen
import com.lucas.fittrack.ui.screen.HomeScreen
import com.lucas.fittrack.ui.screen.SettingsScreen
import com.lucas.fittrack.ui.viewmodel.HomeViewModel
import com.lucas.fittrack.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FitTrackApp(
    viewModel: HomeViewModel
) {
    val navController = rememberNavController()

    fun navigateTo(
        destination: AppDestination
    ) {
        val isEditingMeal =
            viewModel.uiState.value.editingMealId != null

        if (
            isEditingMeal &&
            destination != AppDestination.Diet
        ) {
            viewModel.cancelMealEditing()
        }

        navController.navigate(
            destination.route
        ) {
            popUpTo(
                navController.graph.startDestinationId
            ) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
    }

    val destinations = listOf(
        AppDestination.Home,
        AppDestination.Diet,
        AppDestination.History,
        AppDestination.Settings
    )

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry
            ?.destination
            ?.route

    val navigationItems = listOf(
        NavigationItem(
            destination = AppDestination.Home,
            iconRes = R.drawable.ic_home
        ),
        NavigationItem(
            destination = AppDestination.Diet,
            iconRes = R.drawable.ic_diet
        ),
        NavigationItem(
            destination = AppDestination.History,
            iconRes = R.drawable.ic_history
        ),
        NavigationItem(
            destination = AppDestination.Settings,
            iconRes = R.drawable.ic_settings
        )
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationItems.forEach { item ->

                    NavigationBarItem(
                        selected =
                            currentRoute == item.destination.route,


                        onClick = {
                            navigateTo(
                                item.destination
                            )
                        },

                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = item.iconRes
                                ),
                                contentDescription =
                                    item.destination.label
                            )
                        },

                        label = {
                            Text(
                                text = item.destination.label
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onEditMeal = { meal ->

                        viewModel.editMeal(meal)

                        navigateTo(
                            AppDestination.Diet
                        )
                    }
                )
            }

            composable(AppDestination.Diet.route) {

                DietScreen(
                    viewModel = viewModel,

                    onSearchTextChange =
                        viewModel::updateFoodSearchText,

                    onCategorySelected =
                        viewModel::selectFoodCategory,

                    onEditingFinished = {
                        navigateTo(
                            AppDestination.Home
                        )
                    }
                )
            }

            composable(AppDestination.History.route) {
                HistoryScreen(
                    viewModel = viewModel
                )
            }

            composable(AppDestination.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}