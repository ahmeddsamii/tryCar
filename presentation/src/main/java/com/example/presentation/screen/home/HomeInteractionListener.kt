package com.example.presentation.screen.home

import com.example.domain.entity.Post

interface HomeInteractionListener {
    fun onClickPost(postId: Int)
    fun onClickRetry()
    fun onClickFavorite(post: Post)
}