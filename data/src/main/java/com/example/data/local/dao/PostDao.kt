package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.local.entity.LocalPost

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<LocalPost>

    @Insert
    fun insertAllPosts(list: List<LocalPost>)

    @Query("DELETE FROM posts")
    fun clearAllPosts()

    @Query("DELETE FROM posts WHERE id = :postId")
    fun deletePostById(postId: Int)
}