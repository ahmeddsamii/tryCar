package com.example.presentation.details

sealed interface DetailsUiEffect {
    data object NavigateBack : DetailsUiEffect
}