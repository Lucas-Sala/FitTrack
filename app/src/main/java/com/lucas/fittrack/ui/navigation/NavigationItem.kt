package com.lucas.fittrack.ui.navigation

import androidx.annotation.DrawableRes

data class NavigationItem(
    val destination: AppDestination,
    @DrawableRes val iconRes: Int
)