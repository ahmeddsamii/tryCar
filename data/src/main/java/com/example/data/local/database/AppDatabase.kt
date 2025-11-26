package com.example.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.PostDao
import com.example.data.local.entity.FavoritePost
import com.example.data.local.entity.LocalPost

@Database(entities = [LocalPost::class, FavoritePost::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}