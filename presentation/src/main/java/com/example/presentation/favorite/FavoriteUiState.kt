package com.example.presentation.favorite

import com.example.domain.entity.Post
import com.example.presentation.shared.ErrorState

data class FavoriteUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorState? = null
)