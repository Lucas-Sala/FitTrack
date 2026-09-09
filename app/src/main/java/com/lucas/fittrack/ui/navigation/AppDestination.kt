package com.lucas.fittrack.ui.navigation

sealed class AppDestination(
    val route: String,
    val label: String
) {
    data object Home : AppDestination(
        route = "home",
        label = "Início"
    )

    data object Diet : AppDestination(
        route = "diet",
        label = "Dieta"
    )

    data object History : AppDestination(
        route = "history",
        label = "Histórico"
    )

    data object Settings : AppDestination(
        route = "settings",
        label = "Ajustes"
    )
}