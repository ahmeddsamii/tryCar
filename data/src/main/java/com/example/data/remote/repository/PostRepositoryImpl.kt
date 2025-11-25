package com.example.data.remote.repository

import com.example.data.remote.mapper.toEntityList
import com.example.data.remote.source.PostRemoteDataSource
import com.example.domain.entity.Post
import com.example.domain.repository.PostRepository
import org.koin.core.annotation.Single

@Single(binds = [PostRepository::class])
class PostRepositoryImpl(
    private val remoteDataSource: PostRemoteDataSource
): PostRepository {
    override suspend fun getAllPosts(): List<Post> {
        return remoteDataSource.getAllPosts().toEntityList()
    }
}