package com.example.data.local.source

import com.example.data.local.dao.PostDao
import com.example.data.local.entity.FavoritePost
import com.example.data.local.entity.LocalPost
import org.koin.core.annotation.Single

@Single(binds = [PostLocalDataSource::class])
class PostLocalDataSourceImpl(
    private val postDao: PostDao
) : PostLocalDataSource {

    override fun insertAllPosts(localPosts: List<LocalPost>) {
        postDao.insertAllPosts(localPosts)
    }

    override fun clearAllPosts() {
        postDao.clearAllPosts()
    }

    override fun getPostById(postId: Int): LocalPost {
        return postDao.getPostById(postId)
    }

    override fun insertPost(post: LocalPost) {
        postDao.insertPost(post)
    }

    override fun getAllFavoritePosts(): List<FavoritePost> {
        return postDao.getAllFavoritePosts()
    }

    override fun insertFavoritePost(post: FavoritePost) {
        postDao.insertFavoritePost(post)
    }

    override fun getAllPosts(): List<LocalPost> {
        return postDao.getAllPosts()
    }
}