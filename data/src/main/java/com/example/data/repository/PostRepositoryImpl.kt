package com.example.data.repository

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

        return runCatching {
            val remotePosts = remoteDataSource.getAllPosts().toEntityList()

            clearAllPosts()
            insertAllPosts(remotePosts)

            remotePosts
        }.getOrElse {
            val localPosts = localDataSource.getAllPosts().toEntityPostsFromLocal()
            localPosts.ifEmpty { throw it }
        }
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

    private fun clearAllPosts() = localDataSource.clearAllPosts()

    private fun insertAllPosts(posts: List<Post>) = localDataSource.insertAllPosts(posts.toLocal())

}