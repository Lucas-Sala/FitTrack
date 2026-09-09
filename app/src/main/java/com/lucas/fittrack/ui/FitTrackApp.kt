package com.lucas.fittrack.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lucas.fittrack.ui.navigation.AppDestination
import com.lucas.fittrack.ui.screen.DietScreen
import com.lucas.fittrack.ui.screen.HistoryScreen
import com.lucas.fittrack.ui.screen.HomeScreen
import com.lucas.fittrack.ui.screen.SettingsScreen
import com.lucas.fittrack.ui.viewmodel.HomeViewModel

@Composable
fun FitTrackApp(
    viewModel: HomeViewModel
) {
    val navController = rememberNavController()

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

    Scaffold(
        bottomBar = {
            NavigationBar {

                destinations.forEach { destination ->

                    NavigationBarItem(
                        selected =
                            currentRoute == destination.route,

                        onClick = {
                            navController.navigate(
                                destination.route
                            ) {
                                launchSingleTop = true
                            }
                        },

                        icon = {
                            Text("•")
                        },

                        label = {
                            Text(destination.label)
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
                    viewModel = viewModel
                )
            }

            composable(AppDestination.Diet.route) {
                DietScreen(
                    viewModel = viewModel
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