package com.example.presentation.shared

sealed class ErrorState {
    data object NoInternet : ErrorState()
    data class RequestFailed(val message: String? = "Request failed") : ErrorState()
}