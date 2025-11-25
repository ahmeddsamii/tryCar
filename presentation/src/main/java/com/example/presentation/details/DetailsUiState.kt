package com.example.presentation.details

import com.example.domain.entity.Comment
import com.example.presentation.shared.ErrorState

data class DetailsUiState(
    val isLoading: Boolean = false,
    val comments: List<Comment> = emptyList(),
    val error: ErrorState? = null
)