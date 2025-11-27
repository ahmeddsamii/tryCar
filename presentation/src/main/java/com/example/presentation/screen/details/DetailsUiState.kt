package com.example.presentation.screen.details

import com.example.domain.entity.Comment
import com.example.presentation.shared.base.ErrorState

data class DetailsUiState(
    val isLoading: Boolean = false,
    val comments: List<Comment> = emptyList(),
    val error: ErrorState? = null
)