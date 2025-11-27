package com.example.presentation.screen.home

import com.example.domain.entity.Post
import com.example.presentation.shared.base.ErrorState

data class HomeUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: ErrorState? = null
)