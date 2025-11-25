package com.example.presentation.home

sealed interface HomeUiEffect {
    data class DetailsNavigation(val postId: Int) : HomeUiEffect
}