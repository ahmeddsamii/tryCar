package com.example.domain.repository

import com.example.domain.entity.Post

interface PostRepository {
    suspend fun getAllPosts(): List<Post>
}