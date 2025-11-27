package com.example.presentation.screen.favorite

import com.example.domain.entity.Post
import com.example.presentation.shared.base.ErrorState

data class FavoriteUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorState? = null
)