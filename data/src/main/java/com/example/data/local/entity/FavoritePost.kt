package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_posts")
data class FavoritePost(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isSynced: Boolean = false
)