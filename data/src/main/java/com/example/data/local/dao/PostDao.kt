package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FavoritePost
import com.example.data.local.entity.LocalPost

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<LocalPost>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPosts(list: List<LocalPost>)

    @Query("DELETE FROM posts")
    fun clearAllPosts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: LocalPost)

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: Int): LocalPost

    @Query("DELETE FROM posts WHERE id = :postId")
    fun deletePostById(postId: Int)

    @Query("DELETE FROM favorite_posts WHERE id = :postId")
    fun deleteFavoritePostById(postId: Int)

    @Query("SELECT * FROM favorite_posts")
    fun getAllFavoritePosts(): List<FavoritePost>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoritePost(post: FavoritePost)
}