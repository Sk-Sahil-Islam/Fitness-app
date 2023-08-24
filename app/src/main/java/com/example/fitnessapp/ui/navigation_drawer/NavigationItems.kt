package com.example.fitnessapp.ui.navigation_drawer


import androidx.compose.ui.graphics.vector.ImageVector


data class NavigationItems(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

