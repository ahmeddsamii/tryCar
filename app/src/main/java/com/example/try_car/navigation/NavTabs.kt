package com.example.try_car.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.presentation.Route
import com.example.try_car.R
import com.example.try_car.ui.component.NavigationTab
import com.example.try_car.ui.component.TabIcons

@Composable
fun navTabs(): List<NavigationTab<Any>> {
    return listOf(
        NavigationTab(
            tabIcons = TabIcons(
                idleIcon = painterResource(R.drawable.ic_post),
                selectedIcon = painterResource(R.drawable.ic_filled_post),
            ),
            destination = Route.Home
        ),
        NavigationTab(
            tabIcons = TabIcons(
                idleIcon = painterResource(R.drawable.ic_tab_bookmark),
                selectedIcon = painterResource(R.drawable.ic_filled_bookmark),
            ),
            destination = Route.Favorite
        )
    )
}

