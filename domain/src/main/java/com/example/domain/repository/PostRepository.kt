package com.example.domain.repository

import com.example.domain.entity.Comment
import com.example.domain.entity.Post

interface PostRepository {
    suspend fun getAllPosts(): List<Post>
    suspend fun getAllCommentsByPostId(postId: Int): List<Comment>
    suspend fun getAllFavoritePosts(): List<Post>
    suspend fun insertFavoritePost(post: Post)
    suspend fun deletePostById(postId: Int)
    suspend fun getFavoritePostById(postId: Int)
    suspend fun getPendingFavoritePosts(): List<Post>
    suspend fun markFavoriteAsSynced(postId: Int)
}