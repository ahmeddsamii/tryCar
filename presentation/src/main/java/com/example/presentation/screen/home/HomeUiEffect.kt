package com.example.presentation.screen.home

sealed interface HomeUiEffect {
    data class DetailsNavigation(val postId: Int) : HomeUiEffect
}