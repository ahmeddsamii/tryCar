package com.example.data.local.source

import com.example.data.local.entity.FavoritePost
import com.example.data.local.entity.LocalPost

interface PostLocalDataSource {
    fun insertAllPosts(localPosts: List<LocalPost>)
    fun getAllPosts(): List<LocalPost>
    fun clearAllPosts()
    fun getPostById(postId: Int): LocalPost
    fun insertPost(post: LocalPost)
    fun getAllFavoritePosts(): List<FavoritePost>
    fun insertFavoritePost(post: FavoritePost)
    fun deleteFavoritePostById(postId: Int)
}