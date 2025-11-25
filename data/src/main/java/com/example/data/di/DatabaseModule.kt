package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.dao.PostDao
import com.example.data.local.database.AppDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {

    @Single
    fun provideRoomDatabase(applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Single
    fun provideUserDao(db: AppDatabase): PostDao {
        return db.postDao()
    }
}