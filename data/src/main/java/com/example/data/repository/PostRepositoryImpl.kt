package com.example.data.repository

import android.util.Log
import com.example.data.local.source.PostLocalDataSource
import com.example.data.remote.mapper.favoritesToEntityList
import com.example.data.remote.mapper.toEntityList
import com.example.data.remote.mapper.toEntityPostsFromLocal
import com.example.data.remote.mapper.toFavorite
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
        return try {
            val remotePosts = remoteDataSource.getAllPosts().toEntityList()

            runCatching {
                clearAllPosts()
                insertAllPosts(remotePosts)
            }.onFailure {
                Log.d("TAG", "getAllPosts: ${it.message}")
            }

            remotePosts
        } catch (exception: Exception) {
            localDataSource.getAllPosts().toEntityPostsFromLocal().ifEmpty { throw exception }
        }
    }

    private fun clearAllPosts() {
        localDataSource.clearAllPosts()
    }

    private fun insertAllPosts(posts: List<Post>) {
        localDataSource.insertAllPosts(posts.toLocal())
    }

    override suspend fun getAllCommentsByPostId(postId: Int): List<Comment> {
        return remoteDataSource.getAllCommentsByPostId(postId).toEntityList()
    }

    override suspend fun getAllFavoritePosts(): List<Post> {
        return localDataSource.getAllFavoritePosts().favoritesToEntityList()
    }

    override suspend fun insertFavoritePost(post: Post) {
        localDataSource.insertFavoritePost(post.toFavorite())
    }

    override suspend fun deletePostById(postId: Int) {
        localDataSource.deleteFavoritePostById(postId)
    }

    override suspend fun getFavoritePostById(postId: Int) {
        localDataSource.getFavoritePostById(postId)
    }

    override suspend fun getPendingFavoritePosts(): List<Post> {
        return localDataSource.getPendingFavoritePosts().favoritesToEntityList()
    }

    override suspend fun markFavoriteAsSynced(postId: Int) {
        localDataSource.markFavoriteAsSynced(postId)
    }
}