package com.example.try_car.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.Route
import com.example.presentation.details.DetailsScreen
import com.example.presentation.favorite.FavoriteScreen
import com.example.presentation.home.HomeScreen
import com.example.try_car.R
import com.example.try_car.ui.component.NavBar
import com.example.try_car.ui.component.NavigationTab
import com.example.try_car.ui.component.TabIcons

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainGraph() {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            bottomBar = {
                NavBar(
                    navDestinations = listOf(
                        NavigationTab(
                            tabIcons = TabIcons(
                                idleIcon = painterResource(R.drawable.ic_launcher_foreground),
                                selectedIcon = painterResource(R.drawable.ic_launcher_background)
                            ),
                            destination = Route.Home
                        ),
                        NavigationTab(
                            tabIcons = TabIcons(
                                idleIcon = painterResource(R.drawable.ic_launcher_foreground),
                                selectedIcon = painterResource(R.drawable.ic_launcher_background)
                            ),
                            destination = Route.Favorite
                        )
                    ),
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    navController = navController,
                    startDestination = Route.Home,
                ) {
                    composable<Route.Home> {
                        HomeScreen(navController)
                    }

                    composable<Route.Favorite> {
                        FavoriteScreen()
                    }

                    composable<Route.Details> {
                        DetailsScreen()
                    }
                }
            }
        }
    }
}

val LocalNavController = compositionLocalOf<NavController> {
    error("NavController not provided")
}