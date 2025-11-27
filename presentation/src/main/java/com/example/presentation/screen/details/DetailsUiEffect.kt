package com.example.presentation.screen.details

sealed interface DetailsUiEffect {
    data object NavigateBack : DetailsUiEffect
}