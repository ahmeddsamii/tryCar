package com.example.data.local.source

import com.example.data.local.entity.LocalPost

interface PostLocalDataSource {
    fun insertAllPosts(localPosts: List<LocalPost>)
    fun getAllPosts(): List<LocalPost>
    fun clearAllPosts()
}