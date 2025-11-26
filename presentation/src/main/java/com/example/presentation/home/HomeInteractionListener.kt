package com.example.presentation.home

import com.example.domain.entity.Post

interface HomeInteractionListener {
    fun onClickPost(postId: Int)
    fun onClickRetry()
    fun onClickFavorite(post: Post)
}