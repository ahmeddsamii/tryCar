package com.example.data.repository

import com.example.data.local.source.PostLocalDataSource
import com.example.data.remote.mapper.toEntityList
import com.example.data.remote.mapper.toEntityPostsFromLocal
import com.example.data.remote.mapper.toLocal
import com.example.data.remote.source.PostRemoteDataSource
import com.example.domain.entity.Comment
import com.example.domain.entity.Post
import com.example.domain.repository.PostRepository
import org.koin.core.annotation.Single

@Single(binds = [PostRepository::class])
class PostRepositoryImpl(
    private val remoteDataSource: PostRemoteDataSource,
    private val localDataSource: PostLocalDataSource
) : PostRepository {
    override suspend fun getAllPosts(): List<Post> {

        return runCatching {
            val remotePosts = remoteDataSource.getAllPosts().toEntityList()

            localDataSource.clearAllPosts()
            localDataSource.insertAllPosts(remotePosts.toLocal())

            remotePosts
        }.getOrElse {
            localDataSource.getAllPosts().toEntityPostsFromLocal()
        }
    }

    override suspend fun getAllCommentsByPostId(postId: Int): List<Comment> {
        return remoteDataSource.getAllCommentsByPostId(postId).toEntityList()
    }
}