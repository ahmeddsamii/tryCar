package com.example.try_car.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.Route
import com.example.presentation.home.HomeScreen

@Composable
fun MainGraph() {

    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavController provides navController,
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = Route.Home,
            ) {
                composable<Route.Home> {
                    HomeScreen()
                }
            }
        }
    }
}

val LocalNavController = compositionLocalOf<NavController> {
    error("NavController not provided")
}
