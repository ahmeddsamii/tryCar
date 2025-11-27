package com.example.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Favorite : Route

    @Serializable
    data class Details(val postId: Int) : Route
}