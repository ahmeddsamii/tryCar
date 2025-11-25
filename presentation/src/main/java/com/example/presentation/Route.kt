package com.example.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home: Route

    @Serializable
    data object Favorite: Route
}