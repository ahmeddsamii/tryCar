package com.example.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute

data class NavigationTab<out T>(
    val tabIcons: TabIcons,
    val destination: T,
)

data class TabIcons(
    val idleIcon: Painter,
    val selectedIcon: Painter
)

data class NavBarColors(
    val backgroundColor: Color,
    val selectedIconColor: Color,
    val idleIconColor: Color
)

object NavBarDefaults {
    @Composable
    fun navBarColors() = NavBarColors(
        backgroundColor = MaterialTheme.colorScheme.surface,
        selectedIconColor = MaterialTheme.colorScheme.primary,
        idleIconColor = MaterialTheme.colorScheme.onSurface
    )
}

private object NavBarDimens {
    val iconSize = 24.dp
    val clickableAreaSize = 42.dp
    val verticalPadding = 7.dp
}

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    navDestinations: List<NavigationTab<Route>>,
    navBackStackEntry: NavBackStackEntry? = null,
    navBarColors: NavBarColors = NavBarDefaults.navBarColors()
) {
    val navController = LocalNavController.current

    val checkIfSelected: (Route) -> Boolean = { destination ->
        navBackStackEntry?.destination?.hasRoute(destination::class) == true
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(navBarColors.backgroundColor)
            .navigationBarsPadding()
            .padding(vertical = NavBarDimens.verticalPadding),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navDestinations.forEach { item ->
            NavBarItem(
                tabIcons = item.tabIcons,
                selectedIconColor = navBarColors.selectedIconColor,
                idleIconColor = navBarColors.idleIconColor,
                onClick = {
                    navController.navigate(item.destination) {
                        launchSingleTop = true
                        restoreState = false
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = false
                        }
                    }
                },
                isSelected = checkIfSelected(item.destination)
            )
        }
    }
}

@Composable
private fun NavBarItem(
    tabIcons: TabIcons,
    isSelected: Boolean,
    selectedIconColor: Color,
    idleIconColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(NavBarDimens.clickableAreaSize)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = if (isSelected) tabIcons.selectedIcon else tabIcons.idleIcon,
            modifier = Modifier.size(NavBarDimens.iconSize),
            contentDescription = null,
            tint = if (isSelected) selectedIconColor else idleIconColor
        )
    }
}