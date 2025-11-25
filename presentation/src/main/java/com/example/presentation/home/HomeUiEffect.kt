package com.example.presentation.home

sealed interface HomeUiEffect {
    data object NavigationBack : HomeUiEffect
}